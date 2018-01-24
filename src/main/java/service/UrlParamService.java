package service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        urlParam.setOpt(Long.parseLong(getField(urlReceive, 2)));

        return urlParam;
    }

    private String getField(String urlReceive, int index) {
        String[] docTypes = urlReceive.split("=");
        docTypes = docTypes[docTypes.length - index].split("&");

        return docTypes[0];
    }

    public void getFileContent(String originalFileId, String partnerId, HttpServletResponse response) throws CoreException, IOException {
        if(StringUtils.isEmpty(originalFileId)){
            throw new CoreException("INVALID_REQUEST_INPUT", "FileId invalid");
        }

        if(StringUtils.isEmpty(partnerId)){
            throw new CoreException("INVALID_REQUEST_INPUT", "PartnerId invalid");
        }

        FileCore fileCore = fileRepo.getPathByFileIdAndPartnerId(originalFileId, partnerId);
        if(fileCore == null){
            throw new CoreException("INVALID_REQUEST_STORAGE","FileCore doesn't exist");
        }

        getContentFile(fileCore, response);
    }

    private void getContentFile(FileCore fileCore, HttpServletResponse response) throws CoreException, IOException {
        File file = new File(fileCore.getPath());
        if(!file.exists()){
            throw new CoreException("INVALID_REQUEST_STORAGE", "File doesn't exist");
        }

        log.info("===>>>File Name: " + file.getName());
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        log.info("mimetype : "+mimeType);

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));

        response.setContentLength((int)file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
