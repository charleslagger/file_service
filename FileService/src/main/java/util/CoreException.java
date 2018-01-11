/**
 * Created CoreException.java at 11:12:18 AM by hungvq
 * TODO
 */
package util;

/**
 * @author hungvq
 *
 */
public class CoreException extends Exception {

	private static final long serialVersionUID = 1L;
	private String error_code;
	
	public CoreException(String errCode, String message) {
		super(message);
		this.setError_code(errCode);
		
	}
	
	public CoreException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.error_code = errCode;
	}
	/**
	 * @return the error_code
	 */
	public String getError_code() {
		return error_code;
	}
	/**
	 * @param error_code the error_code to set
	 */
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
}
