package io.toast.tk.runtime.action.item;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;

import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.bean.ArgumentDescriptor;
import io.toast.tk.runtime.bean.ActionItem.ActionTypeEnum;

public class ActionItemValueProvider {

	private static final Logger LOG = LogManager.getLogger(ActionItemValueProvider.class);

	private Map<ActionTypeEnum, IValueHandler> map;
	
	@Inject
	public ActionItemValueProvider(){
		this.map = new HashMap<>(10);
		fillMap();
	}

	private void fillMap() {
		map.put(ActionTypeEnum.json, new JSONValueHandler());
		map.put(ActionTypeEnum.xml, new XMLValueHandler());
		map.put(ActionTypeEnum.string, new StringValueHandler());
		map.put(ActionTypeEnum.web, new WebValueHandler());
		map.put(ActionTypeEnum.swing, new SwingValueHandler());
	}

	public IValueHandler get(ArgumentDescriptor descriptor, IActionItemRepository repository) {
		IValueHandler handler = null;
		if(descriptor != null && descriptor.typeEnum != null){
			handler = map.get(descriptor.typeEnum);
			if(handler != null){
				handler.setRepository(repository);
				handler.setArgumentDescriptor(descriptor);
			}
			else{
				LOG.warn("No value hanlder found for : " + descriptor.typeEnum);
			}
		}
		return handler;
	}
}