package service;

import db.entities.UrlParam;
import db.repo.FileRepo;
import db.repo.PartnerRepo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import exception.CoreException;

@Service
public class UrlParamService {
    private Base64 base64 = new Base64();
    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private PartnerRepo partnerRepo;

    public UrlParam generateUrl(String urlReceive) throws CoreException{
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
}
