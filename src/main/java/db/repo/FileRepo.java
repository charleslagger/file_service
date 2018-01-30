package db.repo;

import db.entities.FileCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class FileRepo {
    private static final String COLLECTION_NAME = "Files";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveFile(FileCore fileCore) {
        mongoTemplate.save(fileCore, COLLECTION_NAME);
    }

    public FileCore getPathByFileIdAndPartnerId(String originalFileId, String partnerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("FileName").is(originalFileId).and("PartnerId").is(partnerId));

        List<FileCore> fileCores = mongoTemplate.find(query, FileCore.class, "Files");
        return CollectionUtils.isEmpty(fileCores) ? null : fileCores.get(0);
    }
    
    public FileCore getPathByFileId(String originalFileId) {
    	Query query = new Query();
    	query.addCriteria(Criteria.where("FileName").is(originalFileId));
    	
    	List<FileCore> fileCores = mongoTemplate.find(query, FileCore.class, "Files");
    	return CollectionUtils.isEmpty(fileCores) ? null : fileCores.get(0);
    }

}
