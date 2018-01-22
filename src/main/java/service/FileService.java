package service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import com.vega.core.CoreException;
import db.entities.FileCore;
import db.entities.Partner;
import db.entities.UrlParam;
import db.repo.FileRepo;
import db.repo.PartnerRepo;
import db.repo.seq.SeqFileRepo;
import db.repo.seq.SeqPartnerRepo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import status.PartnerStatus;

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
                || urlParam.getOrderId() == null || urlParam.getOrderId() < 0 || StringUtils.isEmpty(urlParam.getAckUrl())
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

        if(fileType.equals("exe") || fileType.equals("php") || fileType.equals("sh")|| fileType.equals("com")){
            throw new CoreException("INVALID_REQUEST_INPUT", "File's format prevented");
        }
    }

    private String fileName(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().indexOf(fileTypeName(multipartFile)) - 1);
    }

    private String fileTypeName(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename().split("\\.")[multipartFile.getOriginalFilename().split("\\.").length - 1];
    }
}
