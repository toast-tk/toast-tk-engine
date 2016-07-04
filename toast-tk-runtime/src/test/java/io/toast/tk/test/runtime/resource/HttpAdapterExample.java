package io.toast.tk.test.runtime.resource;

import com.google.gson.Gson;

import io.toast.tk.core.adapter.ActionAdapterKind;
import io.toast.tk.core.annotation.Action;
import io.toast.tk.core.annotation.ActionAdapter;
import io.toast.tk.dao.core.report.FailureResult;
import io.toast.tk.dao.core.report.SuccessResult;
import io.toast.tk.dao.domain.api.test.ITestResult;
import io.toast.tk.test.bean.User;

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
