package db.entities;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UrlParam {
	private String docTypeName;// fName
	private String partnerId;
	private Long orderId;
	private String ackUrl;
	private String mKey;
	private Short opt;// field to determine request from core is loan request(0) or contract(2) or
	// disbursement(1)

	private MultipartFile multipartFile;

	public UrlParam() {
		super();
	}

	public UrlParam(String docTypeName, String partnerId, Long orderId, String ackUrl, String mKey, Short opt,
			MultipartFile multipartFile) {
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

	// @JsonBackReference
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public static class UrlParams {

		public UrlParams() {

		}

		public UrlParams(List<UrlParam> urlParams) {
			super();
			this.urlParams = urlParams;
		}

		// private Long optType;
		private List<UrlParam> urlParams;

		// public Long getOptType() {
		// return optType;
		// }
		//
		// public void setOptType(Long optType) {
		// this.optType = optType;
		// }

		public List<UrlParam> getUrlParams() {
			return urlParams;
		}

		public void setUrlParams(List<UrlParam> urlParams) {
			this.urlParams = urlParams;
		}
	}
}
