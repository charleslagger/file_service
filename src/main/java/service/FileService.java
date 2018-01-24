package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.vega.core.CoreException;

import db.entities.FileCore;
import db.entities.FileCore.AckUpload;
import db.entities.Partner;
import db.entities.UrlParam;
import db.repo.FileRepo;
import db.repo.PartnerRepo;
import db.repo.seq.SeqFileRepo;
import db.repo.seq.SeqPartnerRepo;
import status.PartnerStatus;
import util.Constant;

@Service
public class FileService {
	private Logger log = Logger.getLogger(getClass());
	private static final String FILE_SEQ_KEY = "Files";
	private static final String PARTNER_SEQ_KEY = "Partnerss";
	private Base64 base64 = new Base64();
	private final String HOST;
	private final Path DEST;
	@Autowired
	private FileRepo fileRepo;

	@Autowired
	private PartnerRepo partnerRepo;

	@Autowired
	private SeqFileRepo seqFileRepo;

	@Autowired
	private SeqPartnerRepo seqPartnerRepo;

	@Autowired
	private NotificationService notificationService;

	public FileService(Environment env) {
		HOST = env.getProperty("host");
		DEST = Paths.get(System.getProperty(env.getProperty("storage")) + File.separator + "file");
		initDest();
	}

	private void initDest() {
		File dir = new File(DEST.toAbsolutePath().toString());
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public void saveFile(UrlParam urlParam) throws CoreException {
		MultipartFile multipartFile = urlParam.getMultipartFile();
		if (multipartFile == null) {
			throw new CoreException("INVALID_REQUEST_INPUT", "File input invalid");
		}
		preventFileDamage(multipartFile);

		if (StringUtils.isEmpty(urlParam.getDocTypeName()) || StringUtils.isEmpty(urlParam.getPartnerId())
				|| urlParam.getOrderId() == null || urlParam.getOrderId() < 0
				|| StringUtils.isEmpty(urlParam.getAckUrl()) 
				|| (!urlParam.getAckUrl().equals(Constant.ACK_CONTRACTS) && !urlParam.getAckUrl().equals(Constant.ACK_DISBURSEMENT)
				&& !urlParam.getAckUrl().equals(Constant.ACK_LOAN_DOCUMENTS))
				|| StringUtils.isEmpty(urlParam.getmKey())
				|| urlParam.getOpt() == null || urlParam.getOpt() < 0) {
			throw new CoreException("INVALID_REQUEST_INPUT", "Input param invalid");
		}

		long currentTimeUpload = Calendar.getInstance().getTimeInMillis();

		String fileFullName = fileName(multipartFile) + "_" + currentTimeUpload + "." + fileTypeName(multipartFile);

		FileCore file = new FileCore();
		file.setId(seqFileRepo.getNextSequenceId(FILE_SEQ_KEY));
		file.setFileName(fileFullName);
		file.setContent(urlParam.getDocTypeName());
		file.setDateCreated(String.valueOf(currentTimeUpload));
		file.setOrderId(urlParam.getOrderId());
		file.setPartnerId(urlParam.getPartnerId());
		file.setPath(DEST.toString() + File.separator + fileFullName);

		Partner partner = new Partner();
		partner.setId(seqPartnerRepo.getNextSequenceId(PARTNER_SEQ_KEY));
		partner.setName(new String(base64.encode(urlParam.getPartnerId().getBytes())));
		partner.setAuthenKey(urlParam.getmKey());
		partner.setStatus(PartnerStatus.PARTNER_ACTIVE);

		partner.setIp((HOST.split(":"))[1].substring(2));

		fileRepo.saveFile(file);
		partnerRepo.savePartner(partner);

		storageFile(multipartFile, fileFullName);

		// TODO:send noti
		sendInfoToCore(urlParam, file, partner);
	}

	private void storageFile(MultipartFile multipartFile, String fileName) {
		File file = new File(DEST.toString() + File.separator + fileName);
		BufferedOutputStream stream = null;

		try {
			stream = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		byte[] bytes = new byte[0];
		try {
			bytes = multipartFile.getBytes();
			stream.write(bytes);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void preventFileDamage(MultipartFile multipartFile) throws CoreException {

		String fileType = fileTypeName(multipartFile);

		if (fileType.equals("exe") || fileType.equals("php") || fileType.equals("sh") || fileType.equals("com")) {
			throw new CoreException("INVALID_REQUEST_INPUT", "File's format prevented");
		}
	}

	private String fileName(MultipartFile multipartFile) {
		return multipartFile.getOriginalFilename().substring(0,
				multipartFile.getOriginalFilename().indexOf(fileTypeName(multipartFile)) - 1);
	}

	private String fileTypeName(MultipartFile multipartFile) {
		return multipartFile.getOriginalFilename().split("\\.")[multipartFile.getOriginalFilename().split("\\.").length
				- 1];
	}

	public void sentNotificationLoanDocuments(FileCore.LoanDocuments loanDocuments) throws CoreException {
		if (loanDocuments == null || StringUtils.isEmpty(loanDocuments.getAckUrl())
				|| loanDocuments.getLoanDocuments() == null) {
			throw new CoreException("INVALID_REQUEST_INPUT", "List LoanDocument's request invalid");
		}

		for (FileCore.LoanDocument loanDocument : loanDocuments.getLoanDocuments()) {
			if (StringUtils.isEmpty(loanDocument.getDocumentTypeName())
					|| StringUtils.isEmpty(loanDocument.getOriginalFileId()) || loanDocument.getOrderId() < 0L) {
				throw new CoreException("INVALID_REQUEST_INPUT", "LoanDocument's request invalid");
			}
		}

		notificationService.sendLoanDocuments(loanDocuments);

	}

	public void sentNotificationContract(AckUpload ackUpload) throws CoreException {
		if (ackUpload == null || StringUtils.isEmpty(ackUpload.getAckUrl())
				|| ackUpload.getAcknowledgeUploaded() == null) {
			throw new CoreException("INVALID_REQUEST_INPUT", "AcknowledgeUploaded's contract request invalid");
		}

		notificationService.sendAcknowledgeUploadContract(ackUpload);
	}

	public void sentNotificationDisbusement(String ackUrl, Long orderId, String disbursementDocumentId) throws CoreException{
		if(StringUtils.isEmpty(ackUrl) || (orderId != null && orderId < 1) || StringUtils.isEmpty(disbursementDocumentId)) {
			throw new CoreException("INVALID_REQUEST_INPUT", "Input request invalid");
		}
		
		notificationService.sendDisbursement(ackUrl, "orderId", orderId, "disbursementDocumentId", disbursementDocumentId);

	}

	private void sendInfoToCore(UrlParam urlParam, FileCore file, Partner partner) {
		if() {
			throw new CoreException("INVALID_REQUEST_INPUT", "Url param invalid");
		}
	}
}
