package db.seq;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
* Go to ROBO 3T get query below to insert id and seq to "file_seq" collection(table): 
* db.file_seq.insert({_id: "Files",seq: 0})
* */

@Document(collection = "file_seq")
public class FileSeq {
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
