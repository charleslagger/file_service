package db.seq;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
*
* db.partner_seq.insert({_id: "Partnerss",seq: 0})
* */

@Document(collection = "partner_seq")
public class PartnerSeq {
    @Id
    private String id;

    private long seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }
}
