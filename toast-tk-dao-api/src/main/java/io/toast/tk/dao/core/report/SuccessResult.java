package io.toast.tk.dao.core.report;

public class SuccessResult extends TestResult{

	public SuccessResult() {
		super("OK");
		this.setIsSuccess(true);
		this.resultKind =  ResultKind.SUCCESS;
	}

	public SuccessResult(final String message) {
		this();
		this.message = message;
	}

	public SuccessResult(
		final String message,
		final String img
	) {
		this(message);
		this.setScreenShot(img);
	}
}