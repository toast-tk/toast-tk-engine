package com.synaptix.toast.adapter.web.component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.synaptix.toast.adapter.swing.AbstractSwingPage;
import com.synaptix.toast.adapter.web.IWebComponentFactory;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.adapter.AutoWebType;
import com.synaptix.toast.core.runtime.IFeedableWebPage;
import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.core.runtime.IWebElementDescriptor;
import com.synaptix.toast.core.runtime.IWebElementDescriptor.LocationMethod;

/**
 * 
 * Page fixture abstraction
 * Initializes adapter elements's locators 
 * based on provided markdown definitions
 */
public abstract class AbstractWebPage implements IFeedableWebPage {
	
	private static final Logger LOG;

	private static final ServiceLoader<IWebComponentFactory> factoryLoader;
	
	static {
		LOG = LogManager.getLogger(AbstractSwingPage.class);
		factoryLoader = ServiceLoader.load(IWebComponentFactory.class);
	}
	
	public String beanClassName;

	protected final Map<String, IWebElementDescriptor> elements;

	protected final Map<String, IWebAutoElement<?>> autoElements;

	private String pageName;
	
	public IWebElementDescriptor descriptor;

	private SynchronizedDriver driver;
	
	public AbstractWebPage() {
		this.elements = new HashMap<>();
		this.autoElements = new HashMap<>();
	}
	
	@Override
	public void initElement(final IWebElementDescriptor e) {
		initElement(e.getName(), e.getType().name(), e.getMethod().name(), e.getLocator(), e.getPosition());
	}

	/**
	 * Done for each page element based on a feeder
	 * 
	 * @param name
	 * @param method
	 * @param locator
	 * @param position
	 */
	protected void initElement(
		final String name,
		final String type,
		final String method,
		final String locator,
		final Integer position
	) {
		final DefaultWebElement defaultWebElement = buildWebElementDescriptor(name,type, method, locator, position);
		elements.put(name, defaultWebElement);
		try {
			final IWebElementDescriptor iWebElement = elements.get(name);
			final IWebComponentFactory factory = factoryLoader.iterator().next();
			if(factory == null) {
				throw new IllegalAccessError("No Web Element Factory declared !");
			}
			if(iWebElement != null) {
				final IWebAutoElement<?> execAutoClass = factory.getElement(iWebElement);
				execAutoClass.setContainer(this);
				initBeanFields(name, execAutoClass);
				autoElements.put(name, execAutoClass);
			}
			else {
				// throw something
			}
		}
		catch(final Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	protected static DefaultWebElement buildWebElementDescriptor(
		final String name,
		final String type, 
		final String method, 
		final String locator, 
		final Integer position
	) {
		String kind;
		String referenceName = null;
		if(isReferenceComponent(type)) {
			kind = StringUtils.split(type, ":")[0];
			referenceName = StringUtils.split(type, ":")[1];
		}
		else {
			kind = type;
		}
		final LocationMethod locationMethod = method == null ? LocationMethod.CSS : LocationMethod.valueOf(method);
		final AutoWebType webType = AutoWebType.valueOf(kind);
		return new DefaultWebElement(name, webType, locator, locationMethod, position, referenceName);
	}

	private static boolean isReferenceComponent(String type) {
		return type.contains(":");
	}

	private void initBeanFields(
		final String name,
		final IWebAutoElement<?> execAutoClass
	) {
		for(Field f : getClass().getFields()) {
			Class<?> automationClass = f.getType();
			if(IWebAutoElement.class.isAssignableFrom(automationClass)) {
				if(f.getName().equals(name)) {
					try {
						BeanUtils.setProperty(this, name, execAutoClass);
					}
					catch(Exception e) {
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public IWebElementDescriptor getElement(String token) {
		return elements.get(token);
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	@Override
	public IWebAutoElement<?> getAutoElement(
		String token) {
		return autoElements.get(token);
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(
		String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	@Override
	public List<IWebElementDescriptor> getChildren() {
		return new ArrayList<>(elements.values());
	}

	@Override
	public void setDescriptor(IWebElementDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public IWebElementDescriptor getDescriptor() {
		return this.descriptor;
	}
	
	@Override
	public void setContainer(IFeedableWebPage webPage){
		//NO OP
	}

	@Override
	public IFeedableWebPage getContainer(){
		return this;
	}

	@Override
	public void setDriver(SynchronizedDriver sDvr) {
		this.driver = sDvr;
		for(IWebAutoElement<?> el : autoElements.values()) {
			el.setDriver(sDvr);
		}		
	}
	
	@Override
	public Object getWebElement() {
		return driver.find(descriptor);
	}
	
	@Override
	public List getAllWebElements() {
		return driver.findAll(descriptor);
	}
}