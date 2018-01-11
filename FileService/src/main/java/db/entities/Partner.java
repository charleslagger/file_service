package db.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Partners")
public class Partner {
    private long id;
    private String name;
    private String ip;
    private String authenKey;
    private short status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAuthenKey() {
        return authenKey;
    }

    public void setAuthenKey(String authenKey) {
        this.authenKey = authenKey;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
