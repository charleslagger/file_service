package service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
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

	public void getFile(String originalFileId, String partnerId, HttpServletResponse response)
			throws CoreException, IOException {
		if (StringUtils.isEmpty(originalFileId)) {
			throw new CoreException("INVALID_REQUEST_INPUT", "FileId invalid");
		}

		if (StringUtils.isEmpty(partnerId)) {
			throw new CoreException("INVALID_REQUEST_INPUT", "PartnerId invalid");
		}

		FileCore fileCore = fileRepo.getPathByFileIdAndPartnerId(originalFileId, partnerId);
		if (fileCore == null) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "FileCore doesn't exist");
		}
		
		viewFile(fileCore, response);
	}
	
	public String getFileContent(String originalFileId, String partnerId)
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

	private void viewFile(FileCore fileCore, HttpServletResponse response) throws CoreException, IOException {
		File file = new File(fileCore.getPath());
		if (!file.exists()) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "File doesn't exist");
		}

		log.info("===>>>File Name: " + file.getName());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		log.info("mimetype : " + mimeType);

		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	private String getContentFile(FileCore fileCore) throws CoreException, IOException {
		File file = new File(fileCore.getPath());
		if (!file.exists()) {
			throw new CoreException("INVALID_REQUEST_STORAGE", "File doesn't exist");
		}

		log.info("===>>>File Name: " + file.getName());

		StringBuilder contentBuilder = new StringBuilder();
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = new String();


			while ((str = new String(in.readLine().getBytes("ISO-8859-1"), "UTF8")) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());

		} catch (IOException e) {
			System.out.println(e.getMessage());

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

//		StringBuilder contentBuilder = new StringBuilder();
//		try {
//		    BufferedReader in = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(file), "UTF-16"));
//		    String str = new String();
//		    while ((str = in.readLine()) != null) {
//		        contentBuilder.append(str);
//		    }
//		    in.close();
//		} catch (IOException e) {
//		}


		byte[] utf8 = contentBuilder.toString().getBytes("UTF-8");

		System.out.println("===========>>>>>" + new String(utf8, "UTF-8"));
		return contentBuilder.toString();
	}
}