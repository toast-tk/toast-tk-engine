package io.toast.tk.core.net.request;

public interface IIdRequest {

	/**
	 * current request id
	 * @return
	 */
	String getId();
	
	/**
	 * A representive screenshot of the system under test at the time the result is computed
	 * @return
	 */
	String getBase64ScreenShot();
}