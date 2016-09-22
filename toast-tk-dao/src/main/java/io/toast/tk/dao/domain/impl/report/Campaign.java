package io.toast.tk.dao.domain.impl.report;

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

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestPage;

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

	@Reference(ignoreMissing=true)
	private List<ITestPage> testCases;

	@Override
	public void setId(final Object object) {
		if(object == null) {
			this.id = null;
		}else if(object instanceof String){
			setId(new ObjectId((String)object));
		}else if(object instanceof ObjectId){
			setId((ObjectId) object);
		}
	}
	
	public void setId(final ObjectId id) {
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

	public void setIteration(final short iteration) {
		this.iteration = iteration;
	}

	public Date getExecDay() {
		return execDay;
	}

	public void setExecDay(final Date execDay) {
		this.execDay = execDay;
	}

	@Override
	public List<ITestPage> getTestCases() {
		return testCases;
	}

	public void setTestCases(final List<ITestPage> testCases) {
		this.testCases = testCases;
	}

	public void setTestCasesImpl(final List<TestPage> testCases) {
		this.testCases = testCases != null ? new ArrayList<>(testCases) : null;
	}

	@Override
	@PrePersist
	public void prePersist() {
		this.execDay = new Date();
		++iteration;
	}

	public void setHadINTDb(final boolean hasDB) {
		this.hasINTDb = hasDB;
	}

	public boolean isHasINTDb() {
		return hasINTDb;
	}
	
	
}