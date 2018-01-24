package db.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Files")
public class FileCore {
	@Id
	@Field("_id")
	private long id;

	@Field("FileName")
	private String fileName;

	@Field("Path")
	private String path;

	@Field("DateCreated")
	private String dateCreated;

	@Field("Content")
	private String content;

	@Field("OrderId")
	private long orderId;

	@Field("PartnerId")
	private String partnerId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public static class LoanDocument {
		private Long orderId;
		private String documentTypeName;
		private String originalFileId;

		public Long getOrderId() {
			return orderId;
		}

		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public String getDocumentTypeName() {
			return documentTypeName;
		}

		public void setDocumentTypeName(String documentTypeName) {
			this.documentTypeName = documentTypeName;
		}

		public String getOriginalFileId() {
			return originalFileId;
		}

		public void setOriginalFileId(String originalFileId) {
			this.originalFileId = originalFileId;
		}
	}

	public static class LoanDocuments {
		private String ackUrl;
		private List<LoanDocument> loanDocuments;

		public String getAckUrl() {
			return ackUrl;
		}

		public void setAckUrl(String ackUrl) {
			this.ackUrl = ackUrl;
		}

		public List<LoanDocument> getLoanDocuments() {
			return loanDocuments;
		}

		public void setLoanDocuments(List<LoanDocument> loanDocuments) {
			this.loanDocuments = loanDocuments;
		}
	}

	public static class AckUpload {
		private String ackUrl;
		private AcknowledgeUploaded acknowledgeUploaded;

		public String getAckUrl() {
			return ackUrl;
		}

		public void setAckUrl(String ackUrl) {
			this.ackUrl = ackUrl;
		}

		public AcknowledgeUploaded getAcknowledgeUploaded() {
			return acknowledgeUploaded;
		}

		public void setAcknowledgeUploaded(AcknowledgeUploaded acknowledgeUploaded) {
			this.acknowledgeUploaded = acknowledgeUploaded;
		}

		public static class AcknowledgeUploaded {
			private Long orderId;
			private String contractDocumentId;
			private Short type;
			private List<ProductContractPattern> productContractPatterns;

			public Long getOrderId() {
				return orderId;
			}

			public void setOrderId(Long orderId) {
				this.orderId = orderId;
			}

			public String getContractDocumentId() {
				return contractDocumentId;
			}

			public void setContractDocumentId(String contractDocumentId) {
				this.contractDocumentId = contractDocumentId;
			}

			public Short getType() {
				return type;
			}

			public void setType(Short type) {
				this.type = type;
			}

			public List<ProductContractPattern> getProductContractPatterns() {
				return productContractPatterns;
			}

			public void setProductContractPatterns(List<ProductContractPattern> productContractPatterns) {
				this.productContractPatterns = productContractPatterns;
			}

			public static class ProductContractPattern {
				private String patternName;
				private String originalFileID;
				private Long productId;

				public String getPatternName() {
					return patternName;
				}

				public void setPatternName(String patternName) {
					this.patternName = patternName;
				}

				public String getOriginalFileID() {
					return originalFileID;
				}

				public void setOriginalFileID(String originalFileID) {
					this.originalFileID = originalFileID;
				}

				public Long getProductId() {
					return productId;
				}

				public void setProductId(Long productId) {
					this.productId = productId;
				}

			}
		}
	}
}
