package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;

import com.synaptix.toast.core.net.request.IIdRequest;

public interface ISwingWidgetActionHandler<E extends Component, O, R extends IIdRequest> {
	
	O handle(final E component, final R command);
}