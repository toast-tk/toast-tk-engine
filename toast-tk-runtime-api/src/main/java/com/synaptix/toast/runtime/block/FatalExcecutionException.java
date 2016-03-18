package com.synaptix.toast.runtime.block;

public class FatalExcecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	public FatalExcecutionException() {}  

	public FatalExcecutionException(String message) {  
		super(message); 
	}  

	public FatalExcecutionException(Throwable cause) {  
		super(cause); 
	}  

	public FatalExcecutionException(String message, Throwable cause) {  
		super(message, cause); 
	} 
}
