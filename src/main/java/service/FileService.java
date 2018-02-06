package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.vega.core.CoreException;
import com.vega.core.CoreResponse;

import db.entities.FileCore;
import db.entities.FileCore.AckUpload;
import db.entities.FileCore.AckUpload.AcknowledgeUploaded;
import db.entities.FileCore.AckUpload.AcknowledgeUploaded.ProductContractPattern;
import db.entities.FileCore.LoanDocument;
import db.entities.FileCore.LoanDocuments;
import db.entities.Partner;
import db.entities.UrlParam;
import db.entities.UrlParam.UrlParams;
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
	private static final String PARTNER_SEQ_KEY = "Partners";
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

	public CoreResponse saveFile(UrlParams responseCore) throws CoreException {

		List<FileCore> fileCores = new LinkedList<>();
		List<Partner> partners = new LinkedList<>();

		for (UrlParam urlParam : responseCore.getUrlParams()) {
			MultipartFile multipartFile = urlParam.getMultipartFile();
			if (multipartFile == null) {
				throw new CoreException("INVALID_REQUEST_INPUT", "File input invalid");
			}
			preventFileDamage(multipartFile);

			if (StringUtils.isEmpty(urlParam.getDocTypeName()) || StringUtils.isEmpty(urlParam.getPartnerId())
					|| urlParam.getOrderId() == null || urlParam.getOrderId() < 0
					|| StringUtils.isEmpty(urlParam.getAckUrl())
					|| (!urlParam.getAckUrl().equals(Constant.ACK_CONTRACTS)
							&& !urlParam.getAckUrl().equals(Constant.ACK_DISBURSEMENT)
							&& !urlParam.getAckUrl().equals(Constant.ACK_LOAN_DOCUMENTS))
					|| StringUtils.isEmpty(urlParam.getmKey()) || urlParam.getOpt() == null || urlParam.getOpt() < 0) {
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

			fileCores.add(file);
			partners.add(partner);
		}

		// Write test case
		return sendInfoToCore(responseCore.getUrlParams(), fileCores, partners);
	}
	
	public CoreResponse saveFile(UrlParam urlParam) throws CoreException {

			MultipartFile multipartFile = urlParam.getMultipartFile();
			if (multipartFile == null) {
				throw new CoreException("INVALID_REQUEST_INPUT", "File input invalid");
			}
			preventFileDamage(multipartFile);

			if (StringUtils.isEmpty(urlParam.getDocTypeName()) || StringUtils.isEmpty(urlParam.getPartnerId())
					|| urlParam.getOrderId() == null || urlParam.getOrderId() < 0
					|| StringUtils.isEmpty(urlParam.getAckUrl())
					|| (!urlParam.getAckUrl().equals(Constant.ACK_CONTRACTS)
							&& !urlParam.getAckUrl().equals(Constant.ACK_DISBURSEMENT)
							&& !urlParam.getAckUrl().equals(Constant.ACK_LOAN_DOCUMENTS))
					|| StringUtils.isEmpty(urlParam.getmKey()) || urlParam.getOpt() == null || urlParam.getOpt() < 0) {
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

			return sendInfoToCore(urlParam, file, partner);
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

		notificationService.sendListLoanDocuments(loanDocuments);

	}

	public void sentNotificationContract(AckUpload ackUpload) throws CoreException {
		if (ackUpload == null || StringUtils.isEmpty(ackUpload.getAckUrl())
				|| ackUpload.getAcknowledgeUploaded() == null) {
			throw new CoreException("INVALID_REQUEST_INPUT", "AcknowledgeUploaded's contract request invalid");
		}

		notificationService.sendAcknowledgeUploadContract(ackUpload);
	}

	public void sentNotificationDisbusement(String ackUrl, Long orderId, String disbursementDocumentId)
			throws CoreException {
		if (StringUtils.isEmpty(ackUrl) || (orderId != null && orderId < 1)
				|| StringUtils.isEmpty(disbursementDocumentId)) {
			throw new CoreException("INVALID_REQUEST_INPUT", "Input request invalid");
		}

		notificationService.sendDisbursement(ackUrl, "orderId", orderId, "disbursementDocumentId",
				disbursementDocumentId);

	}

	private CoreResponse sendInfoToCore(List<UrlParam> urlParams, List<FileCore> files, List<Partner> partners) {
		LoanDocuments loanDocuments = new LoanDocuments();
		AckUpload ackUpload = new AckUpload();

		List<LoanDocument> lds = new LinkedList<>();
		List<ProductContractPattern> productContractPatterns = new LinkedList<>();
		AcknowledgeUploaded acknowledgeUploaded = new AcknowledgeUploaded();

		int i = 0;
		for (UrlParam urlParam : urlParams) {
			switch (urlParam.getAckUrl()) {
			case Constant.ACK_LOAN_DOCUMENTS:

				if (StringUtils.isEmpty(loanDocuments.getAckUrl())) {
					loanDocuments.setAckUrl(urlParam.getAckUrl());
				}

				LoanDocument loanDocument = new LoanDocument();
				loanDocument.setDocumentTypeName(urlParam.getDocTypeName());
				loanDocument.setOriginalFileId(files.get(i).getFileName());
				loanDocument.setOrderId(urlParam.getOrderId());

				lds.add(loanDocument);
				break;

			case Constant.ACK_CONTRACTS:

				if (StringUtils.isEmpty(ackUpload.getAckUrl())) {
					ackUpload.setAckUrl(urlParam.getAckUrl());
				}

				if (StringUtils.isEmpty(acknowledgeUploaded.getContractDocumentId())
						&& (acknowledgeUploaded.getOrderId() == null || acknowledgeUploaded.getOrderId() < 1)
						&& (acknowledgeUploaded.getType() == null || acknowledgeUploaded.getType() != 2)) {
					acknowledgeUploaded.setOrderId(urlParam.getOrderId());
					acknowledgeUploaded.setContractDocumentId(files.get(i).getFileName());
					acknowledgeUploaded.setType(urlParam.getOpt());
				}

				ProductContractPattern productContractPattern = new ProductContractPattern();
				productContractPattern.setOriginalFileID(files.get(i).getFileName());
				productContractPattern.setPatternName(urlParam.getDocTypeName());
				productContractPattern.setProductId(urlParam.getOrderId());

				productContractPatterns.add(productContractPattern);

				break;

			case Constant.ACK_DISBURSEMENT:

				return notificationService.sendDisbursement(urlParam.getAckUrl(), "orderId", urlParam.getOrderId(),
						"disbursementDocumentId", files.get(i).getFileName());

			default:
				break;
			}

			i++;
		}

		loanDocuments.setLoanDocuments(lds);
		log.info("==>>LoanDocuments ack: " + loanDocuments.getAckUrl());
		if (!StringUtils.isEmpty(loanDocuments.getAckUrl())) {
			return notificationService.sendListLoanDocuments(loanDocuments);

		}

		acknowledgeUploaded.setProductContractPatterns(productContractPatterns);
		ackUpload.setAcknowledgeUploaded(acknowledgeUploaded);
		log.info("==>>AckUpload ack: " + ackUpload.getAckUrl());

		return notificationService.sendAcknowledgeUploadContract(ackUpload);
	}

	private CoreResponse sendInfoToCore(UrlParam urlParam, FileCore file, Partner partner) {
		LoanDocument loanDocument = new LoanDocument();
		AcknowledgeUploaded acknowledgeUploaded = new AcknowledgeUploaded();

		switch (urlParam.getAckUrl()){
			case Constant.ACK_LOAN_DOCUMENTS:
				loanDocument = new LoanDocument();
				loanDocument.setDocumentTypeName(urlParam.getDocTypeName());
				loanDocument.setOriginalFileId(file.getFileName());
				loanDocument.setOrderId(urlParam.getOrderId());

				return notificationService.sendLoanDocument(urlParam.getAckUrl(), loanDocument);

			case Constant.ACK_CONTRACTS:
				acknowledgeUploaded.setOrderId(urlParam.getOrderId());
				acknowledgeUploaded.setContractDocumentId(file.getFileName());
				acknowledgeUploaded.setType(urlParam.getOpt());

				return notificationService.sendContract(urlParam.getAckUrl(), acknowledgeUploaded);

			case Constant.ACK_DISBURSEMENT:
				
				return notificationService.sendDisbursement(urlParam.getAckUrl(), "orderId", urlParam.getOrderId(),
						"disbursementDocumentId", file.getFileName());

			default:
				return new CoreResponse(true, "ACK Url invalid");
		}
	}
}
