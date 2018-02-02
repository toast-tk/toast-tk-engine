package io.toast.tk.runtime.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptHelper {

	private static final Logger LOG = LogManager.getLogger(EncryptHelper.class);
	
	private EncryptHelper() {
		
	}
	
	public static String encrypt(String password){
		String crypte= "";
		for (int i=0; i<password.length();i++)  {
		    int c=password.charAt(i)^48;  
		    crypte=crypte+(char)c; 
		}
	    return crypte;
	}
	
	public static String decrypt(String password){
		if(password == null) {
			return "";
		}
		
		 String aCrypter= "";
		for (int i=0; i<password.length();i++)  {
		    int c=password.charAt(i)^48;  
		    aCrypter=aCrypter+(char)c; 
		}
		return aCrypter;
	}
}
