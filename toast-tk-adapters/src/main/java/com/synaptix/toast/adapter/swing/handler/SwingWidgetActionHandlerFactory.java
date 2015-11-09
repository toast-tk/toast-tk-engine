package com.synaptix.toast.adapter.swing.handler;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.synaptix.toast.adapter.swing.handler.button.JButtonActionHandler;
import com.synaptix.toast.adapter.swing.handler.button.JCheckBoxActionHandler;
import com.synaptix.toast.adapter.swing.handler.input.JLabelActionHandler;
import com.synaptix.toast.adapter.swing.handler.input.JPasswordFieldActionHandler;
import com.synaptix.toast.adapter.swing.handler.input.JTextAreaActionHandler;
import com.synaptix.toast.adapter.swing.handler.input.JTextFieldActionHandler;
import com.synaptix.toast.adapter.swing.handler.table.JTableActionHandler;


public class SwingWidgetActionHandlerFactory {
	
	private static SwingWidgetActionHandlerFactory instance;
	
	private static Map<Class<? extends Component>, ISwingwidgetActionHandler> map;
	
	static{
		map = new HashMap<Class<? extends Component>, ISwingwidgetActionHandler>();
		map.put(JLabel.class, new JLabelActionHandler());
		map.put(JTextField.class, new JTextFieldActionHandler());
		map.put(JPasswordField.class, new JPasswordFieldActionHandler());
		map.put(JButton.class, new JButtonActionHandler());
		map.put(JCheckBox.class, new JCheckBoxActionHandler());
		map.put(JTextArea.class, new JTextAreaActionHandler());
		map.put(JTable.class, new JTableActionHandler());
	}
	
	private SwingWidgetActionHandlerFactory(){
		
	}
	
	public static SwingWidgetActionHandlerFactory getInstance(){
		if(instance == null){
			instance = new SwingWidgetActionHandlerFactory();
		}
		return instance;
	}

	public ISwingwidgetActionHandler getHandler(
		Component component) {
		for(Map.Entry<Class<? extends Component>, ISwingwidgetActionHandler> entry: map.entrySet()){
			/**entry.getKey().isInstance(component) |*/
			if(entry.getKey().equals(component.getClass())){
				return entry.getValue();
			}
			if(entry.getKey().isAssignableFrom(component.getClass())){
				return entry.getValue();
			}
		}
		return null;
	}

	public boolean hasHandlerFor(
		Class<? extends Component> componentClass) {
		if(map.get(componentClass) != null){
			return true;
		}
		for(Map.Entry<Class<? extends Component>, ?> entry: map.entrySet()){
			if(entry.getKey().isAssignableFrom(componentClass)){
				return true;
			}
		}
		return false;
	}
//	
//	private isSupportedComponentClass(Class<? extends Component> componentClass){
//		
//	}
	
}
