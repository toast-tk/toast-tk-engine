package com.synaptix.toast.adapter.swing.component;

import java.util.List;
import java.util.UUID;

import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;
import com.synaptix.toast.core.runtime.ISwingElement;
import com.synaptix.toast.dao.domain.api.test.ITestResult;
import com.synaptix.toast.dao.domain.api.test.ITestResult.ResultKind;


public class SwingTableElement extends SwingAutoElement implements HasClickAction {

	public SwingTableElement(
		ISwingElement element,
		IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingTableElement(
		ISwingElement element) {
		super(element);
	}

	public ITestResult find(
		List<TableCommandRequestQueryCriteria> criteria)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.find(criteria)
			.with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build();
		return frontEndDriver.processAndWaitForValue(request);
	}

	public ITestResult find(
		String lookUpColumn,
		String lookUpValue,
		String outputColumn)
		throws Exception {
		outputColumn = outputColumn == null ? lookUpColumn : outputColumn;
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.find(lookUpColumn, lookUpValue, outputColumn)
			.with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build();
		frontEndDriver.process(request);
		return frontEndDriver.processAndWaitForValue(request);
	}

	public ITestResult count()
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.count().with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build();
		frontEndDriver.process(request);
		return frontEndDriver.processAndWaitForValue(request);
	}

	@Override
	public ITestResult click()
		throws Exception {
		boolean res = exists();
		final String requestId = UUID.randomUUID().toString();
		ITestResult result = frontEndDriver.processAndWaitForValue(new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name())
			.click().build());
		result.setResultKind(res && result.getMessage().equals(ResultKind.SUCCESS.name()) ? ResultKind.SUCCESS : ResultKind.ERROR);
		return result;
	}

	@Override
	public void dbClick() {
		throw new IllegalAccessError("Method not implemented !");
	}

	public String doubleClick(
		String column,
		String value)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		frontEndDriver.process(new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.doubleClick(column, value).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build());
		return null;
	}

	public ITestResult selectMenu(
		String menu,
		String column,
		String value)
		throws Exception {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.selectMenu(menu, column, value).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build();
		return frontEndDriver.processAndWaitForValue(request);
	}
}
