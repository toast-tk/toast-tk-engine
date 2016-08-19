package io.toast.tk.dao.domain.impl.repository;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Index;
import com.github.jmkgreen.morphia.annotations.Indexes;

import io.toast.tk.dao.domain.api.repository.IElement;
import io.toast.tk.dao.domain.impl.common.BasicEntityBean;

@Entity(value = "elements")
@Indexes({
	@Index(value = "name")
})
public class ElementImpl extends BasicEntityBean implements IElement{

	public String type;

	public String locator;

	public String method;

	public int position;

	public ElementImpl() {
		this.type = "";
		this.locator = "";
		this.name = "";
		this.method = "";
		this.position = 0;
	}
}