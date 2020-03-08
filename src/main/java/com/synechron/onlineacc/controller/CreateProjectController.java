package com.synechron.onlineacc.controller;

import static com.synechron.onlineacc.util.AppConstants.URI_PIPELINE;
import static com.synechron.onlineacc.util.AppConstants.URI_PIPELINE_INT_LAYER;
import static com.synechron.onlineacc.util.AppConstants.URI_PIPELINE_REGULAR;
import static com.synechron.onlineacc.util.Constant.TICKET_JOB_CREATE_BODY;
import static com.synechron.onlineacc.util.Constant.TICKET_JOB_CREATE_COMPLETED_LABEL;
import static com.synechron.onlineacc.util.Constant.TICKET_JOB_CREATE_STARTED_LABEL;
import static com.synechron.onlineacc.util.Constant.TICKET_JOB_CREATE_TITLE;
import static com.synechron.onlineacc.util.Constant.TICKET_ORG_NAME;
import static com.synechron.onlineacc.util.Constant.TICKET_REPO_NAME;

import java.security.Principal;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.synechron.onlineacc.service.CreateProjectService;
import com.synechron.onlineacc.service.DefectService;
import com.synechron.onlineacc.service.JenkinsService;
import com.synechron.onlineacc.service.SourceCodeService;
import com.synechron.onlineacc.util.CustomGlobalContext;
import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;
import com.synechron.onlineacc.util.Util;

@Controller
@RequestMapping(value = URI_PIPELINE)
public class CreateProjectController {
	Logger L = LoggerFactory.getLogger(CreateProjectController.class);

	private SourceCodeService sourceCodeService;
	@Autowired public void setProjectService(SourceCodeService sourceCodeService) { this.sourceCodeService = sourceCodeService; }

	private DefectService defectService;
	@Autowired public void setDefectService(DefectService defectService) { this.defectService = defectService; }

	private JenkinsService jenkinsService;
	@Autowired public void setJenkinsService(JenkinsService jenkinsService) { this.jenkinsService = jenkinsService; }

	private CreateProjectService createProjectService;
	@Autowired public void setCreateProjectService(CreateProjectService createProjectService) { this.createProjectService = createProjectService; }

	@RequestMapping(value = URI_PIPELINE_REGULAR, method = RequestMethod.GET)
	public String createAppsRegular(Model model) {
		L.debug("54 : Start : CreateProjectController.createAppsRegular(...)");

		model.addAttribute("projectName", "");
		model.addAttribute("technology", ""); // TODO : Send a list of technologies from controller instead of hardcoding in the UI (createPipelineForAppsRegular.html)
		model.addAttribute("newOrgName", ""); // TODO : Send a list of organization names from controller instead of hardcoding in the UI (createPipelineForAppsRegular.html)
		model.addAttribute("testURL", "");

		L.debug("61 : End : CreateProjectController.createAppsRegular(...)");
		return "createPipelineForAppsRegular";
	}

	@RequestMapping(value = URI_PIPELINE_REGULAR, method = RequestMethod.POST)
	public String createAppsRegular(
			@RequestParam(value="projectName", required=true) String strProjectName
			, @RequestParam(value="technology", required=true) ProjectType projectType // JAVA
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			, @RequestParam(value="distributionList", required=true) String distributionList
			, @RequestParam(value="testURL", required=true) String testURL
			, Principal principal
	) throws Exception {
		L.info("Start : CreateProjectController.createAppsRegular(...) : projectType = {}, newOrgName = {}, distributionList = {}", projectType, newOrgName.toString(), distributionList);
		try {
//			Open a Ticket
			Issue issue = defectService.create(TICKET_ORG_NAME, TICKET_REPO_NAME, Util.createDefectInfo(TICKET_JOB_CREATE_TITLE, projectType, newOrgName), TICKET_JOB_CREATE_STARTED_LABEL, Util.createDefectInfo(TICKET_JOB_CREATE_BODY, projectType, newOrgName));

//			Create Git repository
			sourceCodeService.gitFork(projectType, newOrgName);
			String strURLNewGitProject = createProjectService.getNewGitUrl(newOrgName, projectType);

//			Create Organization Folder in Jenkins if it doesn't exits
			jenkinsService.createFolder(newOrgName);

//			Create Jenkins Job
			jenkinsService.createJob(newOrgName, projectType, strProjectName, strURLNewGitProject);

//			Create Jenkins Job
			jenkinsService.triggerJob(newOrgName, strProjectName);

//			Close Ticket
			defectService.closeTicket(CustomGlobalContext.getGitToken(), TICKET_ORG_NAME, TICKET_REPO_NAME, issue, TICKET_JOB_CREATE_COMPLETED_LABEL);

			createProjectService.create(null, newOrgName, projectType, strProjectName, distributionList, testURL, principal);
		} catch (Exception e) {
			L.error("96 : CreateProjectController.createAppsRegular(...) : Exception e = {}", e);
		}
		L.info("End : CreateProjectController.createAppsRegular(...) : projectType = {}, newOrgName = {}, distributionList = {}", projectType, newOrgName.toString(), distributionList);
		return "redirect:/userFront";
	}

	@RequestMapping(value = URI_PIPELINE_INT_LAYER, method = RequestMethod.GET)
	public String createAppsIntegrationApp(Model model) {
		L.debug("104 : Start : CreateProjectController.createAppsIntegrationApp(...)");

		model.addAttribute("projectName", "");
		model.addAttribute("technology", ""); // TODO : Send a list of technologies from controller instead of hardcoding in the UI (createPipelineForAppsIntegration.html)
		model.addAttribute("newOrgName", ""); // TODO : Send a list of organizations from controller instead of hardcoding in the UI (createPipelineForAppsIntegration.html)
		model.addAttribute("testURL", "");

		L.debug("111 : End : CreateProjectController.createAppsIntegrationApp(...)");
		return "createPipelineForAppsIntegration";
	}

	@RequestMapping(value = URI_PIPELINE_INT_LAYER, method = RequestMethod.POST)
	public String createAppsIntegrationApp(
			@RequestParam(value="projectName", required=true) String strProjectName
			, @RequestParam(value="technology", required=true) ProjectType projectType // JAVA
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			, @RequestParam(value="distributionList", required=true) String distributionList
			, @RequestParam(value="testURL", required=true) String testURL
			, Principal principal
	) throws Exception {
		L.info("Start : CreateProjectController.createAppsIntegrationApp(...) : projectType = {}, newOrgName = {}, distributionList = {}", projectType, newOrgName.toString(), distributionList);
		try {
//			Open a Ticket
			Issue issue = defectService.create(TICKET_ORG_NAME, TICKET_REPO_NAME, Util.createDefectInfo(TICKET_JOB_CREATE_TITLE, projectType, newOrgName), TICKET_JOB_CREATE_STARTED_LABEL, Util.createDefectInfo(TICKET_JOB_CREATE_BODY, projectType, newOrgName));

//			Create Git repository
			Repository gitFork = sourceCodeService.gitFork(projectType, newOrgName);
			String strURLNewGitProject = gitFork.getCloneUrl();
			L.info("132 : CreateProjectController.createAppsIntegrationApp(...) : strURLNewGitProject = {}, gitFork.getGitUrl() = {}, gitFork.getUrl() = {}", strURLNewGitProject, gitFork.getGitUrl(), gitFork.getUrl());

//			Create Organization Folder in Jenkins if it doesn't exits
			jenkinsService.createFolder(newOrgName);

//			Create Jenkins Job
			jenkinsService.createJob(newOrgName, projectType, strProjectName, strURLNewGitProject);

//			Create Jenkins Job
			jenkinsService.triggerJob(newOrgName, strProjectName);

//			Close Ticket
			defectService.closeTicket(CustomGlobalContext.getGitToken(), TICKET_ORG_NAME, TICKET_REPO_NAME, issue, TICKET_JOB_CREATE_COMPLETED_LABEL);

			createProjectService.create(null, newOrgName, projectType, strProjectName, distributionList, testURL, principal);
		} catch (Exception e) {
			L.error("146 : CreateProjectController.createAppsIntegrationApp(...) : Exception e = {}", e);
		}
		L.info("End : CreateProjectController.createAppsRegular(...) : projectType = {}, newOrgName = {}, distributionList = {}", projectType, newOrgName.toString(), distributionList);
		return "redirect:/userFront";
	}
}