package io.toast.tk.dao.domain.impl.common;

import java.util.List;

import com.github.jmkgreen.morphia.annotations.Reference;

import io.toast.tk.dao.domain.impl.test.block.ITaggable;

public abstract class BasicTaggableMongoBean extends BasicMongoBean implements ITaggable {

	@Reference
	List<TagImpl> tags;

	public List<TagImpl> getTags() {
		return tags;
	}

	public void setTags(final List<TagImpl> tags) {
		this.tags = tags;
	}
}