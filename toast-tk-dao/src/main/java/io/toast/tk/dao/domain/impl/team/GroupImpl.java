package io.toast.tk.dao.domain.impl.team;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;

@Entity(value = "teams.groups", noClassnameStored = true)
public class GroupImpl extends BasicTaggableMongoBean {

	@Id
	ObjectId id = new ObjectId();
}
