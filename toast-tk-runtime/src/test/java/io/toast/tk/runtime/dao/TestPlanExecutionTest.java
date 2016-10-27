package io.toast.tk.runtime.dao;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.report.Campaign;
import io.toast.tk.dao.domain.impl.report.TestPlanImpl;
import io.toast.tk.dao.domain.impl.repository.ProjectImpl;
import io.toast.tk.dao.domain.impl.test.block.CommentBlock;
import io.toast.tk.dao.domain.impl.test.block.ICampaign;
import io.toast.tk.dao.domain.impl.test.block.ITestPlan;
import io.toast.tk.dao.domain.impl.test.block.TestPage;
import io.toast.tk.dao.service.dao.access.plan.TestPlanDaoService;
import io.toast.tk.dao.service.dao.access.repository.ProjectDaoService;
import io.toast.tk.dao.service.dao.access.test.TestPageDaoService;

public class TestPlanExecutionTest extends EmbeddedMongoDBTestCase{

	static TestPlanDaoService tService;
	static ProjectDaoService pService;
	static TestPageDaoService testService;
	
	@Before
	public void before(){
		tService = getService(TestPlanDaoService.Factory.class);
		pService = getService(ProjectDaoService.Factory.class);
		testService = getService(TestPageDaoService.Factory.class);
		tService.find().asList().forEach(t -> tService.delete(t));
		pService.find().asList().forEach(t -> pService.delete(t));
		
	}
	


	/**
	 *  Test without template without project	
	 * @throws IllegalAccessException 
	 */
	@Test(expected=IllegalAccessException.class)
	public void shouldNotSaveTestPlanWithoutAProject() throws IllegalAccessException {	
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		tService.saveTemplate(plan);
	}
	
	/**
	 * Save a new test plan, iteration must be 0
	 * @throws IllegalAccessException
	 */
	@Test
	public void shouldNewTestPlan() throws IllegalAccessException {	
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(createDefaultProject());
		tService.saveTemplate(plan);
		Assert.assertEquals(0, plan.getIteration());
		Assert.assertFalse(plan.isLast());
	}
	
	/**
	 * Save a new test plan, iteration must be 1 and the plan is the last
	 * @throws IllegalAccessException
	 */
	@Test
	public void shouldNewIteration() throws IllegalAccessException {	
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(createDefaultProject());
		tService.saveTemplate(plan);
		tService.saveNewIteration(plan);
		Assert.assertEquals(1, plan.getIteration());
		Assert.assertTrue(plan.isLast());
	}
	
	/**
	 * Save a new test plan, iteration must be 1 and the plan is the last
	 * Test update test plan while adding a test
	 * @throws IllegalAccessException
	 */
	@Test
	public void shouldNewIterationWithDifferentCampaignName() throws IllegalAccessException {	
		ProjectImpl defaultProject = createDefaultProject();
		
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(defaultProject);
		Campaign cp1 = new Campaign();
		cp1.setName("campaign1");
		plan.setCampaigns(Arrays.asList(cp1));
		tService.saveTemplate(plan);
		Assert.assertEquals("campaign1", plan.getCampaigns().get(0).getName());
		
		TestPlanImpl newPlanWithSameName = new TestPlanImpl();
		newPlanWithSameName.setName("test plan");
		newPlanWithSameName.setProject(defaultProject);
		Campaign cp2 = new Campaign();
		cp2.setName("campaign2");
		newPlanWithSameName.setCampaigns(Arrays.asList(cp2));
		
		ITestPlan resultTestPlan = tService.updateTemplateFromTestPlan(newPlanWithSameName);
		Assert.assertEquals(1, resultTestPlan.getCampaigns().size());
		Assert.assertEquals("campaign2", resultTestPlan.getCampaigns().get(0).getName());
	}
	
	
	
	/**
	 * Test update test plan while removing and a updating a campaign's test
	 * @throws IllegalAccessException
	 */
	@Test
	public void shouldRemoveACampaignAndUpdateATest() throws IllegalAccessException {	
		ProjectImpl defaultProject = createDefaultProject();
		
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(defaultProject);
		Campaign cp1 = new Campaign();
		cp1.setName("campaign1");
		Campaign cp2 = new Campaign();
		cp2.setName("campaign2");
		TestPage page = new TestPage();
		page.addBlock(new CommentBlock());
		page.setName("scenario");
		cp2.setTestCases(Arrays.asList(page));
		plan.setCampaigns(Arrays.asList((ICampaign)cp1,(ICampaign) cp2));
		tService.saveTemplate(plan);
		
		Assert.assertEquals(2, plan.getCampaigns().size());
		
		TestPlanImpl queriedTemplate = tService.getReferenceProjectByName(plan.getName(), defaultProject.getId().toString());
		Assert.assertEquals(plan.getCampaigns().size(), queriedTemplate.getCampaigns().size());
		
		TestPlanImpl newPlanWithSameName = new TestPlanImpl();
		newPlanWithSameName.setName("test plan");
		newPlanWithSameName.setProject(defaultProject);
		Campaign updatedCp = new Campaign();
		updatedCp.setName("campaign2");
		TestPage updatedPage = new TestPage();
		updatedPage.addBlock(new CommentBlock());
		updatedPage.addBlock(new CommentBlock());
		updatedPage.setName("scenario");
		updatedCp.setTestCases(Arrays.asList(updatedPage));
		newPlanWithSameName.setCampaigns(Arrays.asList(updatedCp));
		
		ITestPlan resultTestPlan = tService.updateTemplateFromTestPlan(newPlanWithSameName);
		Assert.assertEquals(page.getIdAsString(), updatedPage.getIdAsString());
		Assert.assertEquals(resultTestPlan.getCampaigns().size(), 1);
		Assert.assertEquals(resultTestPlan.getCampaigns().get(0).getTestCases().get(0).getBlocks().size(), 2);
	}
	
	/**
	 * Test update test plan while renaming a test
	 * @throws IllegalAccessException
	 */
	@Test
	public void shouldRemoveOtherTests() throws IllegalAccessException {	
		ProjectImpl defaultProject = createDefaultProject();
		
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(defaultProject);
		Campaign cp1 = new Campaign();
		cp1.setName("campaign1");
		Campaign cp2 = new Campaign();
		cp2.setName("campaign2");
		TestPage page = new TestPage();
		page.addBlock(new CommentBlock());
		page.setName("scenario");
		cp2.setTestCases(Arrays.asList(page));
		plan.setCampaigns(Arrays.asList((ICampaign)cp1,(ICampaign) cp2));
		tService.saveTemplate(plan);
		
		Assert.assertEquals(2, plan.getCampaigns().size());
		
		TestPlanImpl queriedTemplate = tService.getReferenceProjectByName(plan.getName(), defaultProject.getId().toString());
		Assert.assertEquals(plan.getCampaigns().size(), queriedTemplate.getCampaigns().size());
		
		TestPlanImpl newPlanWithSameName = new TestPlanImpl();
		newPlanWithSameName.setName("test plan");
		newPlanWithSameName.setProject(defaultProject);
		Campaign updatedCp = new Campaign();
		updatedCp.setName("campaign2");
		TestPage updatedPage = new TestPage();
		updatedPage.addBlock(new CommentBlock());
		updatedPage.addBlock(new CommentBlock());
		updatedPage.setName("scenario1");
		updatedCp.setTestCases(Arrays.asList(updatedPage));
		newPlanWithSameName.setCampaigns(Arrays.asList(updatedCp));
		
		ITestPlan resultTestPlan = tService.updateTemplateFromTestPlan(newPlanWithSameName);
		Assert.assertNotEquals(page.getIdAsString(), updatedPage.getIdAsString());
		Assert.assertEquals(resultTestPlan.getCampaigns().size(), 1);
		Assert.assertEquals(resultTestPlan.getCampaigns().get(0).getTestCases().size(), 1);
		Assert.assertEquals(resultTestPlan.getCampaigns().get(0).getTestCases().get(0).getName(), "scenario1");
	}
	
	@Test
	public void shouldZaddAnExistingTest() throws IllegalAccessException {	
		ProjectImpl defaultProject = createDefaultProject();
		
		TestPlanImpl plan = new TestPlanImpl();
		plan.setName("test plan");
		plan.setProject(defaultProject);
		Campaign cp1 = new Campaign();
		cp1.setName("campaign1");
		Campaign cp2 = new Campaign();
		cp2.setName("campaign2");
		TestPage page = new TestPage();
		page.addBlock(new CommentBlock());
		page.setName("scenario");
		cp2.setTestCases(Arrays.asList(page));
		plan.setCampaigns(Arrays.asList((ICampaign)cp1,(ICampaign) cp2));
		tService.saveTemplate(plan);
		
		TestPage savedPage = new TestPage();
		savedPage.addBlock(new CommentBlock());
		savedPage.setName("scenarioSaved");
		testService.save(savedPage);
		
		Assert.assertEquals(2, plan.getCampaigns().size());
		
		TestPlanImpl queriedTemplate = tService.getReferenceProjectByName(plan.getName(), defaultProject.getId().toString());
		Assert.assertEquals(plan.getCampaigns().size(), queriedTemplate.getCampaigns().size());
		
		TestPlanImpl newPlanWithSameName = new TestPlanImpl();
		newPlanWithSameName.setName("test plan");
		newPlanWithSameName.setProject(defaultProject);
		Campaign updatedCp = new Campaign();
		updatedCp.setName("campaign2");
		TestPage updatedPage = new TestPage();
		updatedPage.addBlock(new CommentBlock());
		updatedPage.addBlock(new CommentBlock());
		updatedPage.setName("scenario1");
		updatedCp.setTestCases(Arrays.asList(updatedPage, savedPage));
		newPlanWithSameName.setCampaigns(Arrays.asList(updatedCp));
		
		ITestPlan resultTestPlan = tService.updateTemplateFromTestPlan(newPlanWithSameName);
		Assert.assertNotEquals(page.getIdAsString(), updatedPage.getIdAsString());
		Assert.assertEquals(resultTestPlan.getCampaigns().size(), 1);
		Assert.assertEquals(resultTestPlan.getCampaigns().get(0).getTestCases().size(), 2);
		Assert.assertEquals(resultTestPlan.getCampaigns().get(0).getTestCases().get(0).getName(), "scenario1");
	}

	private ProjectImpl createDefaultProject(){
		ProjectImpl p = new ProjectImpl();
		p.setName("default");
		pService.save(p);
		return p;
	}
}