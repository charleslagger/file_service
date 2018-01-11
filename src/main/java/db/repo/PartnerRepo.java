package db.repo;

import db.entities.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PartnerRepo {
    private static final String COLLECTION_NAME = "Partners";
    private static final String SEQ_FILE = "seq_partner";

    @Autowired
    private MongoTemplate mongoTemplate;

    public void savePartner(Partner partner) {
        mongoTemplate.save(partner, COLLECTION_NAME);
    }
}
