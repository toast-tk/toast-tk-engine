package io.toast.tk.runtime.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import io.toast.tk.core.annotation.EngineEventBus;
import io.toast.tk.runtime.ActionItemRepository;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.report.*;

public class EngineModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(EngineEventBus.class).to(EventBus.class).in(Singleton.class);
		bind(IActionItemRepository.class).to(ActionItemRepository.class).in(Singleton.class);
		bind(IHTMLReportGenerator.class).to(ThymeLeafHTMLReporter.class);
		bind(IProjectHtmlReportGenerator.class).to(ThymeLeafProjectHTMLReporter.class);
		bind(IMailReportSender.class).to(MailReportSender.class);
		install(new RunnerModule());
	}
}