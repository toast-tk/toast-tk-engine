package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;

import com.synaptix.toast.core.net.request.IIdRequest;


public interface ISwingwidgetActionHandler<E extends Component, O, R extends IIdRequest> {
	
	public O handle(E component, R command);
}
