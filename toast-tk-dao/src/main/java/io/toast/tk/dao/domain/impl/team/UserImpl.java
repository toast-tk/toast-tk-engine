package io.toast.tk.dao.domain.impl.team;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.api.team.IGroup;
import io.toast.tk.dao.domain.api.team.IUser;
import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;

@Entity(value = "teams.users", noClassnameStored = true)
public class UserImpl extends BasicTaggableMongoBean implements IUser {

	@Id
	ObjectId id = new ObjectId();

	@Reference(ignoreMissing=true)
	List<IGroup> groups;
	
	@Override
	public List<IGroup> getGroups() {
		return groups;
	}

	public void setGroups(final List<IGroup> groups) {
		this.groups = groups;
	}
}
