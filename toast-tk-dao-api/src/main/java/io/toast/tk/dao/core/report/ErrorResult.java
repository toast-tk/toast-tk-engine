package io.toast.tk.dao.core.report;

public class ErrorResult extends TestResult{

	public ErrorResult() {
		super("KO");
		this.setIsSuccess(false);
		this.setIsError(true);
		this.resultKind =  ResultKind.ERROR;

	}

	public ErrorResult(
		String message) {
		this();
		this.message = message;
	}

	public ErrorResult(
		String message,
		String img) {
		this(message);
		this.setScreenShot(img);
	}
}