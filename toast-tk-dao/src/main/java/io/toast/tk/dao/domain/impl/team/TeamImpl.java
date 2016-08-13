package io.toast.tk.dao.domain.impl.team;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.api.team.ITeam;
import io.toast.tk.dao.domain.api.team.IUser;
import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.test.block.IProject;

@Entity(value = "teams.groups")
@Indexes({
	@Index(value = "name")
})
public class TeamImpl extends BasicTaggableMongoBean implements ITeam {

	@Id
	ObjectId id = new ObjectId();
	
	@Reference(ignoreMissing=true)
	List<IProject> projects;

	@Reference(ignoreMissing=true)
	List<IUser> users;
	
	@Override
	public List<IProject> getProjects() {
		return projects;
	}

	public void setProjects(final List<IProject> projects) {
		this.projects = projects;
	}
	
	@Override
	public List<IUser> getUsers() {
		return users;
	}

	public void setUsers(final List<IUser> users) {
		this.users = users;
	}
}
