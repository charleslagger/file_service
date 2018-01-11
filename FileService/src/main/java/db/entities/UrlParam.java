package db.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UrlParam {
    private String docTypeName;//fName
    private String partnerId;
    private long orderId;
    private String ackUrl;
    private String mKey;
    private long opt;// field to determine request from core is loan request(0) or contract(2) or
    // disbursement(1)

    private MultipartFile multipartFile;

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDoctypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getAckUrl() {
        return ackUrl;
    }

    public void setAckUrl(String ackUrl) {
        this.ackUrl = ackUrl;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public long getOpt() {
        return opt;
    }

    public void setOpt(long opt) {
        this.opt = opt;
    }

    @JsonBackReference
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
