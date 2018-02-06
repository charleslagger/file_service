package service;

import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.vega.core.CoreException;
import db.entities.FileCore;
import db.entities.UrlParam;
import db.repo.FileRepo;
import db.repo.PartnerRepo;

@Service
public class UrlParamService {
	private Logger log = Logger.getLogger(getClass());
	private Base64 base64 = new Base64();
	@Autowired
	private FileRepo fileRepo;
	@Autowired
	private PartnerRepo partnerRepo;

	public UrlParam generateUrl(String urlReceive) throws CoreException {
		urlReceive = new String(base64.decode(urlReceive.getBytes()));

		UrlParam urlParam = new UrlParam();
		urlParam.setDocTypeName(getField(urlReceive, 6));
		urlParam.setPartnerId(getField(urlReceive, 1));
		urlParam.setOrderId(Long.parseLong(getField(urlReceive, 3)));
		urlParam.setAckUrl(getField(urlReceive, 5));
		urlParam.setmKey(getField(urlReceive, 4));
		urlParam.setOpt(Short.parseShort(getField(urlReceive, 2)));

		return urlParam;
	}

	private String getField(String urlReceive, int index) {
		String[] docTypes = urlReceive.split("=");
		docTypes = docTypes[docTypes.length - index].split("&");

		return docTypes[0];
	}

	public File getFile(String originalFileId, String partnerId)
			throws CoreException, IOException {

		FileCore fileCore = fileRepo.getPathByFileIdAndPartnerId(originalFileId, partnerId);
		if (fileCore == null) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "FileCore doesn't exist");
		}
		
		return viewFile(fileCore);
	}
	
	public File getFileContent(String originalFileId, String partnerId)
			throws CoreException, IOException {
		if (StringUtils.isEmpty(originalFileId)) {
			throw new CoreException("INVALID_REQUEST_INPUT", "FileId invalid");
		}

		FileCore fileCore = fileRepo.getPathByFileId(originalFileId);
		if (fileCore == null) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "FileCore doesn't exist");
		}
		
		return getContentFile(fileCore);
	}

	private File viewFile(FileCore fileCore) throws CoreException, IOException {
		File file = new File(fileCore.getPath());
		if (!file.exists()) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "File doesn't exist");
		}

		log.info("===>>>File Name: " + file.getName());
		
		return file;
	}
	
	private File getContentFile(FileCore fileCore) throws CoreException, IOException {
		File file = new File(fileCore.getPath());
		log.info("===>>>Path: " + fileCore.getPath());
		if (!file.exists()) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "File doesn't exist");
		}

		return file;
	}
}