package io.toast.tk.adapter.swing.handler;

import java.awt.Component;

import io.toast.tk.core.net.request.IIdRequest;

public interface ISwingWidgetActionHandler<E extends Component, O, R extends IIdRequest> {
	
	O handle(final E component, final R command);
}