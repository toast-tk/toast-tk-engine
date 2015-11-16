package com.synaptix.toast.test.runtime.resource;

import com.google.gson.Gson;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.report.TestResult.ResultKind;
import com.synaptix.toast.test.bean.User;

public class HttpAdapterExample {
	
	@Action(action = "POST {{com.synaptix.toast.test.bean.User:value:json}} to {{value}}", description = "Post json")
	public TestResult postUrl(User user, String url){
		try {
			String json = new Gson().toJson(user);
			int responseStatusCode = HttpHelper.POST(url, json);
			return new TestResult("Status Reponse Code: " + responseStatusCode, ResultKind.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return new TestResult(e.getMessage());
		}
	}
}
