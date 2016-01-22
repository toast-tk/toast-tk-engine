package com.synaptix.toast.core.net.response;

import java.util.ArrayList;
import java.util.List;

import com.synaptix.toast.core.net.request.IIdRequest;

public class InitResponse implements IIdRequest {

	private String id;

	public String text;

	public String b64ScreenShot;

	public List<String> items = new ArrayList<String>();
	
	public InitResponse(){
		
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getBase64ScreenShot() {
		return b64ScreenShot;
	}
}
