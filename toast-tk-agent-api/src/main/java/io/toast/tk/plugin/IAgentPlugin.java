package io.toast.tk.plugin;

import java.util.Collection;

import com.google.inject.Module;

public interface IAgentPlugin {

	public void boot();

	public Collection<Module> getModules();
}
