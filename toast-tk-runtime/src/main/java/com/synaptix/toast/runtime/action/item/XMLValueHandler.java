package com.synaptix.toast.runtime.action.item;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class XMLValueHandler implements IValueHandler{

	private Injector injector;

	private ArgumentDescriptor descriptor;

	private IActionItemRepository objectRepository;

	@Override
	public void setInjector(final Injector injector) {
		this.injector = injector;
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}

	@Override
	public Object handle(final String group, final String argValue) throws Exception {
		final Class<?> xmlClazz = Class.forName(descriptor.name);
		final JAXBContext jaxbContext = JAXBContext.newInstance(xmlClazz);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final InputStream stream = new ByteArrayInputStream(argValue.getBytes(StandardCharsets.UTF_8));
		final Object value = jaxbUnmarshaller.unmarshal(stream);
		objectRepository.getUserVariables().put(group, value);
		return value;
	}

	@Override
	public void setArgumentDescriptor(final ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}
}