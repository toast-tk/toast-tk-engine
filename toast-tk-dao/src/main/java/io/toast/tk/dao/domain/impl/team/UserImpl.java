package io.toast.tk.dao.domain.impl.team;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;

@Entity(value = "teams.users", noClassnameStored = true)
public class UserImpl extends BasicTaggableMongoBean {

	@Id
	ObjectId id = new ObjectId();

	@Reference
	List<GroupImpl> groups;
}
