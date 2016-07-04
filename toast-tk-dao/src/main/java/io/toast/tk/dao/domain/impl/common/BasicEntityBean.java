package io.toast.tk.dao.domain.impl.common;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Id;

public abstract class BasicEntityBean extends BasicTaggableMongoBean {

	@Id
	protected ObjectId id = new ObjectId();

	public ObjectId getId() {
		return id;
	}

	public void setId(final ObjectId id) {
		this.id = id;
	}
	
    public void setId(final String id) {
        this.id = id == null ? null: new ObjectId(id);
    }

    public String getIdAsString() {
        return id != null ? id.toString() : null;
    }
}