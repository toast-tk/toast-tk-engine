package io.toast.tk.dao.domain.impl.team;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.api.team.ITeam;
import io.toast.tk.dao.domain.api.team.IUser;
import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.test.block.IProject;

@Entity(value = "teams")
@Indexes({
	@Index(value = "name")
})
public class TeamImpl extends BasicTaggableMongoBean implements ITeam {

	@Id
	ObjectId id = new ObjectId();
	
	@Embedded
	List<ProjectImpl> projects;

	@Reference(ignoreMissing=true)
	List<UserImpl> users;
	
	@Override
	public List<IProject> getProjects() {
		return (List)projects;
	}

	public void setProjects(final List<IProject> projects) {
		List theProjects = (List)projects;
		this.projects = theProjects;
	}
	
	@Override
	public List<IUser> getUsers() {
		return (List)users;
	}

	public void setUsers(final List<IUser> users) {
		List theUsers = (List) users;
		this.users = theUsers;
	}
}
