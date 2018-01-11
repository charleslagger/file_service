package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class GenerateSequence {
    @Autowired
    private static MongoTemplate mongoTemplate;

    public static Object getNextSequenceId(long key, Object model) throws SequenceException {

        //get sequence id
        Query query = new Query(Criteria.where("_id").is(key));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        //this is the magic happened.
        model = mongoTemplate.findAndModify(query, update, options, Object.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (model == null) {
            throw new SequenceException("Unable to get sequence id for key : " + key);
        }

        return model;
    }
}
