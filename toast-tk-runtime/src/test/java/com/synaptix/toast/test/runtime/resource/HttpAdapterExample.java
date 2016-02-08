package com.synaptix.toast.test.runtime.resource;

import com.google.gson.Gson;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.report.ErrorResult;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.test.bean.User;

public class HttpAdapterExample {
	
	@Action(action = "POST {{com.synaptix.toast.test.bean.User:value:json}} to {{value}}", description = "Post json")
	public ITestResult postUrl(User user, String url){
		try {
			String json = new Gson().toJson(user);
			int responseStatusCode = HttpHelper.POST(url, json);
			return new SuccessResult("Status Reponse Code: " + responseStatusCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new ErrorResult(e.getMessage());
		}
	}
}
