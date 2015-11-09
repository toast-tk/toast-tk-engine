package com.synaptix.toast.runtime.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.synaptix.toast.core.annotation.EngineEventBus;
import com.synaptix.toast.runtime.ActionItemRepository;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.report.IHTMLReportGenerator;
import com.synaptix.toast.runtime.report.ThymeLeafHTMLReporter;

public class EngineModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(EngineEventBus.class).to(EventBus.class).in(Singleton.class);
		bind(IActionItemRepository.class).to(ActionItemRepository.class).in(Singleton.class);
		bind(IHTMLReportGenerator.class).to(ThymeLeafHTMLReporter.class);
		install(new RunnerModule());
	}
}
