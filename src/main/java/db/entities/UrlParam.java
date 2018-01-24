package db.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UrlParam {
    private String docTypeName;//fName
    private String partnerId;
    private Long orderId;
    private String ackUrl;
    private String mKey;
    private Long opt;// field to determine request from core is loan request(0) or contract(2) or
    // disbursement(1)

    private MultipartFile multipartFile;

    public String getDocTypeName() {
        return docTypeName;
    }

    public void setDocTypeName(String docTypeName) {
        this.docTypeName = docTypeName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
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

    public Long getOpt() {
        return opt;
    }

    public void setOpt(Long opt) {
        this.opt = opt;
    }

    @JsonBackReference
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public static class ResponseCore{
        private Long optType;
        private List<UrlParam> urlParams;

        public Long getOptType() {
            return optType;
        }

        public void setOptType(Long optType) {
            this.optType = optType;
        }

        public List<UrlParam> getUrlParams() {
            return urlParams;
        }

        public void setUrlParams(List<UrlParam> urlParams) {
            this.urlParams = urlParams;
        }
    }
}
