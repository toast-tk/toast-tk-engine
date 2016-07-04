package io.toast.tk.dao.domain.impl.common;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;

import io.toast.tk.dao.domain.impl.test.block.ITaggable;

@Entity(value = "tags", noClassnameStored = true)
public class TagImpl  implements ITaggable {

	@Id
	public ObjectId id = new ObjectId();

	public String name;

	public TagImpl() {

	}

	public TagImpl(
		final ObjectId id,
		final String name
	) {
		this.id = id;
		this.name = name;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(final ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}