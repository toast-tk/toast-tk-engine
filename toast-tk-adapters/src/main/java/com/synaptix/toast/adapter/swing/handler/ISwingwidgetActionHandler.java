package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;

import com.synaptix.toast.core.net.request.CommandRequest;


public interface ISwingwidgetActionHandler<E extends Component, O, R extends CommandRequest> {
	
	public O handle(E component, R command);
}
