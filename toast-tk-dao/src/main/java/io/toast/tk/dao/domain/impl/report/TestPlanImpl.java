package io.toast.tk.dao.domain.impl.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;

@Entity(value = "report.testplans")
@Indexes({
		@Index("name"), @Index("version"), @Index("project")
})
public class TestPlanImpl extends BasicTaggableMongoBean implements ITestPlan {

	@Id
	private ObjectId id;

	private short iteration;

	@Reference(ignoreMissing=true)
	private List<ICampaign> campaigns;

	public String version;

	private Date startDate;

	private Date demoDate;

	private Date prodDate;

	private boolean last;
	
	@Embedded
	public ProjectImpl project;

	@Override
	public ProjectImpl getProject() {
		return project;
	}

	public void setProject(ProjectImpl project) {
		this.project = project;
	}

	public ObjectId getId() {
		return id;
	}

	@Override
	public List<ICampaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(final List<ICampaign> campaigns) {
		this.campaigns = campaigns;
	}

	public void setCampaignsImpl(List<Campaign> campaigns) {
		this.campaigns = campaigns != null ? new ArrayList<>(campaigns) : null;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Date getDemoDate() {
		return demoDate;
	}

	public void setDemoDate(final Date demoDate) {
		this.demoDate = demoDate;
	}

	public Date getProdDate() {
		return prodDate;
	}

	public void setProdDate(final Date prodDate) {
		this.prodDate = prodDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public short getIteration() {
		return iteration;
	}

	public void setIteration(final short iteration) {
		this.iteration = iteration;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(final boolean last) {
		this.last = last;
	}

	@Override
	public void setId(String id) {
		this.id = Strings.isBlank(id) || Strings.isEmpty(id) ? null : new ObjectId(id);
	}

	@Override
	public void setProject(IProject project) {
		this.project = (ProjectImpl)project;
	}

	@Override
	public String getIdAsString() {
		return id == null ? null : id.toString();
	}
}