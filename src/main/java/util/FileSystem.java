package util;

import java.io.Serializable;

import org.springframework.core.io.FileSystemResource;

public class FileSystem implements Serializable{
	private String docTypeName;// fName
	private String partnerId;
	private Long orderId;
	private String ackUrl;
	private String mKey;
	private Short opt;// field to determine request from core is loan request(0) or contract(2) or
	// disbursement(1)

	private FileSystemResource multipartFile;
	
	public FileSystem() {
	
	}

	public FileSystem(String docTypeName, String partnerId, Long orderId, String ackUrl, String mKey, Short opt,
			FileSystemResource multipartFile) {
		super();
		this.docTypeName = docTypeName;
		this.partnerId = partnerId;
		this.orderId = orderId;
		this.ackUrl = ackUrl;
		this.mKey = mKey;
		this.opt = opt;
		this.multipartFile = multipartFile;
	}

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

	public Short getOpt() {
		return opt;
	}

	public void setOpt(Short opt) {
		this.opt = opt;
	}

	public FileSystemResource getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(FileSystemResource multipartFile) {
		this.multipartFile = multipartFile;
	}
}
