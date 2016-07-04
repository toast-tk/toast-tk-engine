package io.toast.tk.runtime.action.item;


import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.bean.ArgumentDescriptor;

public class StringValueHandler implements IValueHandler{

	private ArgumentDescriptor descriptor;

	private IActionItemRepository objectRepository;


	@Override
	public Object handle(final String group, final String argValue) {
		if(group != null && group.startsWith("$")){
			 Object value = argValue == null || argValue == group ? 
					 objectRepository.getUserVariables().get(group) : 
						 	argValue;
			 
			 return value == null ? group : value;
		}
		return group;
	}

	@Override
	public void setArgumentDescriptor(final ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}
}