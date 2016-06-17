package com.synaptix.toast.runtime.block;

public class FatalExcecutionError extends Error {

	private static final long serialVersionUID = 1L;

	public FatalExcecutionError() {}  

	public FatalExcecutionError(String message) {  
		super(message); 
	}  

	public FatalExcecutionError(Throwable cause) {  
		super(cause); 
	}  

	public FatalExcecutionError(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
