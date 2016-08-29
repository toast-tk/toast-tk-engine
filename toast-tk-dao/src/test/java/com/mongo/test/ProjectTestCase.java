package com.mongo.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.team.TeamImpl;
import io.toast.tk.dao.domain.impl.team.UserImpl;
import io.toast.tk.dao.domain.impl.test.block.IProject;
import io.toast.tk.dao.service.dao.access.repository.ProjectDaoService;
import io.toast.tk.dao.service.dao.access.team.TeamDaoService;
import io.toast.tk.dao.service.dao.access.team.UserDaoService;

public class ProjectTestCase extends EmbeddedMongoDBTestCase {

	static ProjectDaoService service;
	
	@Before
	public void cleanDB(){
		createService();
		service.find().asList().stream().forEach(p -> service.delete(p));
	}
	
	@Test
	public void test_a_NonProjectInEmbeddedMongoDb() {
		createService();
		Assert.assertEquals(0, service.find().countAll());
	}
	
	@Test
	public void test_b_shouldSaveOneProjectInEmbeddedMongoDb() {
		createService();
		ProjectImpl prj = new ProjectImpl();
		prj.setName("New Project");
		prj.setDescription("New Description");
		service.save(prj);
		Assert.assertEquals(1, service.find().countAll());
	}
	
	@Test
	public void test_c_shouldfindProjectOwnedByAUser(){
		ProjectImpl prj = new ProjectImpl();
		prj.setName("New Owned Project");
		prj.setDescription("Owned Project Description");
		service.save(prj);
		
		ProjectImpl prjNotOwned = new ProjectImpl();
		prjNotOwned.setName("New Project");
		prjNotOwned.setDescription("Not Owned Project Description");
		service.save(prjNotOwned);
		
		UserImpl user = new UserImpl();
		user.setLogin("login");
		getService(UserDaoService.Factory.class).save(user);
		
		TeamImpl team = new TeamImpl();
		team.setName("default");
		team.setUsers(Arrays.asList(user));
		team.setProjects(Arrays.asList(prj));
		getService(TeamDaoService.Factory.class).save(team);
				
		List<IProject> prjs = getService(UserDaoService.Factory.class).getUserProjects(user);
		Assert.assertNotNull(prjs);
		Assert.assertEquals(1, prjs.size());
		Assert.assertEquals("New Owned Project", prjs.get(0).getName());
	}
	
	
	private void createService(){
		if(service == null){
			ProjectDaoService.Factory repoFactory = injector.getInstance(ProjectDaoService.Factory.class);
			service = repoFactory.create("play_db");
		}
	}
}
