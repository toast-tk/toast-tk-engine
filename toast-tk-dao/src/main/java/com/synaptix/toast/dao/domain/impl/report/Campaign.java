package com.synaptix.toast.dao.domain.impl.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;
import com.github.jmkgreen.morphia.annotations.PrePersist;
import com.github.jmkgreen.morphia.annotations.Reference;
import com.synaptix.toast.dao.domain.impl.common.BasicTaggableMongoBean;
import com.synaptix.toast.dao.domain.impl.test.block.ICampaign;
import com.synaptix.toast.dao.domain.impl.test.block.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.block.TestPage;

@Entity(value = "report.campaigns")
@Indexes({
		@Index(value = "name, -iteration"), @Index("iteration")
})
public class Campaign extends BasicTaggableMongoBean implements ICampaign {

	@Id
	private ObjectId id;

	private short iteration;

	private boolean hasINTDb = true;

	private Date execDay;

	@Reference
	private List<ITestPage> testCases;

	@Override
	public void setId(
		Object object) {
		if(object == null) {
			this.id = null;
		}
	}
	
	public void setId(
		ObjectId id) {
		this.id = id;
	}

	@Override
	public String getIdAsString() {
		return id != null ? id.toString() : null;
	}

	public ObjectId getId() {
		return id;
	}

	public short getIteration() {
		return iteration;
	}

	public void setIteration(
		short iteration) {
		this.iteration = iteration;
	}

	public Date getExecDay() {
		return execDay;
	}

	public void setExecDay(
		Date execDay) {
		this.execDay = execDay;
	}

	public List<ITestPage> getTestCases() {
		return testCases;
	}

	public void setTestCases(
		List<ITestPage> testCases) {
		this.testCases = testCases;
	}

	public void setTestCasesImpl(
		List<TestPage> testCases) {
		if(testCases != null) {
			this.testCases = new ArrayList<ITestPage>();
			for(TestPage testPage : testCases) {
				this.testCases.add(testPage);
			}
		}
		else {
			this.testCases = null;
		}
	}

	@Override
	@PrePersist
	public void prePersist() {
		execDay = new Date();
		iteration++;
	}

	public void setHadINTDb(
		boolean hasDB) {
		this.hasINTDb = hasDB;
	}

	public boolean isHasINTDb() {
		return hasINTDb;
	}
}
