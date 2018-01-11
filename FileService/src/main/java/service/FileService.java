package service;

import db.entities.FileCore;
import db.entities.UrlParam;
import db.repo.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import util.CoreException;

import java.io.File;
import java.util.Calendar;

@Service
public class FileService {
    @Autowired
    private FileRepo fileRepo;

    public void saveFile(UrlParam urlParam) throws CoreException{
        MultipartFile multipartFile = urlParam.getMultipartFile();

        //TODO
        FileCore file = new FileCore();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setContent(multipartFile.getContentType());
        file.setDateCreated(Calendar.getInstance().getTime());
        file.setOrderId(urlParam.getOrderId());
        file.setPartnerId(urlParam.getPartnerId());
//        file.setPath(dirStorage().getAbsolutePath() + java.io.File.separator + multipartFile.getOriginalFilename());
        file.setPath("pathTest");

        fileRepo.saveFile(file);
    }

    private File dirStorage() {
        File dir = new File(System.getProperty("catalina.home") + java.io.File.separator + "file-store");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }
}
