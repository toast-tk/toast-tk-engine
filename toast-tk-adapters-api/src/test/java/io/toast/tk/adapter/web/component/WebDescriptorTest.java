package io.toast.tk.adapter.web.component;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.adapter.web.component.DefaultWebElement;
import io.toast.tk.adapter.web.component.DefaultWebPage;
import io.toast.tk.core.adapter.AutoWebType;
import io.toast.tk.core.runtime.IWebElementDescriptor.LocationMethod;

public class WebDescriptorTest {
	
	@Test
	public void buildDefaultTestPageMethodTest(){
		DefaultWebPage page = new DefaultWebPage();
		DefaultWebElement descriptor = page.buildWebElementDescriptor("pageName", "input", null, ".data", 0);
		Assert.assertNull(descriptor.getReferenceName());
		Assert.assertEquals(LocationMethod.CSS, descriptor.getMethod());
	}
	
	@Test
	public void buildDefaultTestPageReferenceTest(){
		DefaultWebPage page = new DefaultWebPage();
		DefaultWebElement descriptor = page.buildWebElementDescriptor("pageName", "component:Job", "CSS", ".data", 2);
		Assert.assertEquals("Job", descriptor.getReferenceName());
		Assert.assertEquals(LocationMethod.CSS, descriptor.getMethod());
		Assert.assertEquals(AutoWebType.component, descriptor.getType());
	}

}
