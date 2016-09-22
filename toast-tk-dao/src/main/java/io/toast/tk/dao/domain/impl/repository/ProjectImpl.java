package io.toast.tk.dao.domain.impl.repository;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;

import io.toast.tk.dao.domain.impl.common.BasicTaggableMongoBean;
import io.toast.tk.dao.domain.impl.test.block.IProject;

@Entity(value = "projects")
@Indexes({ @Index(value = "name") })
public class ProjectImpl extends BasicTaggableMongoBean implements IProject {

	@Id
	ObjectId id = new ObjectId();
	
	/**
	 * serialization
	 */
	public ProjectImpl(){
		//NO-OP
	}
	
	public ObjectId getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = new ObjectId(id);
	}
}
