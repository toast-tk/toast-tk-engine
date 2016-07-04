package io.toast.tk.dao.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import io.toast.tk.dao.service.dao.access.project.CampaignDaoService;
import io.toast.tk.dao.service.dao.access.project.ProjectDaoService;
import io.toast.tk.dao.service.dao.access.repository.ElementDaoService;
import io.toast.tk.dao.service.dao.access.repository.RepositoryDaoService;
import io.toast.tk.dao.service.dao.access.team.GroupDaoService;
import io.toast.tk.dao.service.dao.access.team.UserDaoService;
import io.toast.tk.dao.service.dao.access.test.CommentBlockDaoService;
import io.toast.tk.dao.service.dao.access.test.ConfigBlockDaoService;
import io.toast.tk.dao.service.dao.access.test.InsertBlockDaoService;
import io.toast.tk.dao.service.dao.access.test.SetupBlockDaoService;
import io.toast.tk.dao.service.dao.access.test.TestBlockDaoService;
import io.toast.tk.dao.service.dao.access.test.TestPageDaoService;
import io.toast.tk.dao.service.dao.common.CommonMongoDaoService;

public class MongoDaoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommonMongoDaoService.class).in(Singleton.class);
		install(new FactoryModuleBuilder().build(RepositoryDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(ElementDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(TestPageDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(CampaignDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(ProjectDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(GroupDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(UserDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(CommentBlockDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(ConfigBlockDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(InsertBlockDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(SetupBlockDaoService.Factory.class));
		install(new FactoryModuleBuilder().build(TestBlockDaoService.Factory.class));
	}
}