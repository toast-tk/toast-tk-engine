package com.synaptix.toast.runtime.action.item;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.runtime.bean.ActionItem.ActionTypeEnum;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class ActionItemValueProvider {

	private static final Logger LOG = LogManager.getLogger(ActionItemValueProvider.class);
	private Map<ActionTypeEnum, IValueHandler> map;
	
	@Inject
	public ActionItemValueProvider(){
		map = new HashMap<ActionTypeEnum, IValueHandler>();
		map.put(ActionTypeEnum.json, new JSONValueHandler());
		map.put(ActionTypeEnum.xml, new XMLValueHandler());
		map.put(ActionTypeEnum.string, new StringValueHandler());
		map.put(ActionTypeEnum.web, new WebValueHandler());
		map.put(ActionTypeEnum.swing, new SwingValueHandler());
	}

	public IValueHandler get(ArgumentDescriptor descriptor, Injector injector) {
		IValueHandler handler = null;
		if(descriptor != null && descriptor.typeEnum != null){
			handler = map.get(descriptor.typeEnum);
			if(handler != null){
				handler.setInjector(injector);
				handler.setArgumentDescriptor(descriptor);
			}else{
				LOG.warn("No value hanlder found for : " + descriptor.typeEnum);
			}
		}
		return handler;
	}
	
}
