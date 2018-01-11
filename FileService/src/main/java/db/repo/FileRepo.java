package db.repo;

import db.entities.FileCore;
import db.entities.UrlParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import util.GenerateSequence;

import java.io.File;

@Repository
public class FileRepo {
    private static final String COLLECTION_NAME = "FintechFiles";
    private static final Long SEQ_FILE = 0L;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveFile(FileCore fileCore) {

        fileCore.setId(((FileCore)(GenerateSequence.getNextSequenceId(SEQ_FILE, fileCore))).getId());
        System.out.println("===>> Id: " + ((FileCore)(GenerateSequence.getNextSequenceId(SEQ_FILE, fileCore))).getId());
        mongoTemplate.save(fileCore, COLLECTION_NAME);
    }
}
