package com.synaptix.toast.test.runtime.resource;

import com.google.gson.Gson;
import com.synaptix.toast.core.adapter.ActionAdapterKind;
import com.synaptix.toast.core.annotation.Action;
import com.synaptix.toast.core.annotation.ActionAdapter;
import com.synaptix.toast.core.report.FailureResult;
import com.synaptix.toast.core.report.SuccessResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.test.bean.User;

@ActionAdapter(value = ActionAdapterKind.service, name ="http-service-adapter")
public class HttpAdapterExample {
	
	@Action(action = "POST {{com.synaptix.toast.test.bean.User:value:json}} to {{value}}", description = "Post json")
	public ITestResult postUrl(User user, String url){
		try {
			String json = new Gson().toJson(user);
			int responseStatusCode = HttpHelper.POST(url, json);
			return new SuccessResult("Status Reponse Code: " + responseStatusCode);
		} catch (Exception e) {
			e.printStackTrace();
			return new FailureResult(e.getMessage());
		}
	}
}
