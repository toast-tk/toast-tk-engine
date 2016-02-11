package com.synaptix.toast.dao.domain.impl.test.block;

import java.util.List;

public interface ICampaign extends ITaggable {

	List<ITestPage> getTestCases();

	String getName();

	void setId(final Object object);

	String getIdAsString();
}