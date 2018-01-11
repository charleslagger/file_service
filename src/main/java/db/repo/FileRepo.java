package db.repo;

import db.entities.FileCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepo {
    private static final String COLLECTION_NAME = "FintechFiles";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveFile(FileCore fileCore) {
        mongoTemplate.save(fileCore, COLLECTION_NAME);
    }
}
