package com.synechron.onlineacc.service;

import static com.synechron.onlineacc.util.Constant.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.synechron.onlineacc.dao.SelfServiceProjectRepository;
import com.synechron.onlineacc.domain.SelfServiceProjectDTO;
import com.synechron.onlineacc.domain.SelfServiceProjectEntity;
import com.synechron.onlineacc.domain.User;
import com.synechron.onlineacc.util.CustomGlobalContext;
import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;

@Service
public class CreateProjectService {
	Logger L = LoggerFactory.getLogger(CreateProjectService.class);

	@Autowired UserService userService;
	@Autowired JenkinsService jenkinsService;
	@Autowired SelfServiceProjectRepository selfServiceProjectRepository;
	@Autowired private Environment env;


	public SelfServiceProjectEntity create(Long id, OrgName newOrgName, ProjectType projectType, String strProjectName, String distributionList, String strTestURL, Principal principal) throws Exception {
		L.info("Start : CreateProjectService.create(...) : id = {}, newOrgName = {}, projectType = {}, strProjectName = {}, strTestURL = {}", id, newOrgName, projectType, strProjectName, strTestURL);
		User user = userService.findByUsername(principal.getName());

		L.info("29 : CreateProjectService.create(...) : UserId = {}", user.getUserId());

		SelfServiceProjectEntity selfServiceProjectEntity = new SelfServiceProjectEntity(id, user.getUserId(), strProjectName, projectType, newOrgName, distributionList, strTestURL);
		selfServiceProjectEntity = selfServiceProjectRepository.save(selfServiceProjectEntity);
		L.info("End : CreateProjectService.create(...) : id = {}, newOrgName = {}, projectType = {}, strProjectName = {}, strTestURL = {}, (null == selfServiceProjectEntity) = {}", id, newOrgName, projectType, strProjectName, strTestURL, (null == selfServiceProjectEntity));
		return selfServiceProjectEntity;
	}

	public List<SelfServiceProjectDTO> get(Principal principal) throws Exception {
		User user = userService.findByUsername(principal.getName());
		L.info("Start : CreateProjectService.get(...) : UserId = {}", user.getUserId());

		List<SelfServiceProjectDTO> lstSelfServiceProjectEntity = new ArrayList<SelfServiceProjectDTO>();
		Iterable<SelfServiceProjectEntity> iterableSelfServiceProjectEntity = selfServiceProjectRepository.findByUserId(user.getUserId());
		Iterator<SelfServiceProjectEntity> iteratorSelfServiceProjectEntity = iterableSelfServiceProjectEntity.iterator();
		SelfServiceProjectEntity objSelfServiceProjectEntity;
		while (iteratorSelfServiceProjectEntity.hasNext()) {
			objSelfServiceProjectEntity = iteratorSelfServiceProjectEntity.next();

//			https://2886795357-9080-frugo01.environments.katacoda.com/job/ravikalla/job/test1/
			String urlJenkinsJob = CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_ORG.replace("<ORG_NAME>", objSelfServiceProjectEntity.getOrgName().toString()) + JENKINS_URI_PROJECT_VIEW.replace("<PROJECT_NAME>", objSelfServiceProjectEntity.getProjectName());

//			https://github.com/ravikalla/JavaTemplateProject
			String urlGitProject = getNewGitUrl(objSelfServiceProjectEntity.getOrgName(), objSelfServiceProjectEntity.getProjectType());

//			https://hub.docker.com/r/ravikalla/JavaTemplateProject
			String urlRepository = getRepositoryUrl(objSelfServiceProjectEntity.getOrgName(), objSelfServiceProjectEntity.getProjectType());

//			https://github.com/ravikalla/devops-tickets/issues
			String urlDefectURL = DEFECT_URL;

//			https://2886795326-9000-cykoria04.environments.katacoda.com/projects
			String urlSonarURL = CustomGlobalContext.getSonarUrl();

			String urlJenkinsJobTrigger = jenkinsService.getJenkinsJobTriggerUrl(objSelfServiceProjectEntity.getOrgName(), objSelfServiceProjectEntity.getProjectName());

			lstSelfServiceProjectEntity.add(new SelfServiceProjectDTO(objSelfServiceProjectEntity, urlJenkinsJob, urlGitProject, urlDefectURL, objSelfServiceProjectEntity.getDistributionList(), urlSonarURL, urlJenkinsJobTrigger, urlRepository));
		}

		L.info("End : CreateProjectService.get(...) : UserId = {}", user.getUserId());

		return lstSelfServiceProjectEntity;
	}

	public String getNewGitUrl(OrgName orgName, ProjectType projectType) {
		return GIT_URL + "/" + orgName.toString() + "/" + env.getProperty("git.templatename." + projectType.toString());
	}
	public String getRepositoryUrl(OrgName orgName, ProjectType projectType) {
		return DOCKER_REPOSITORY_URL + "/" + orgName.toString() + "/" + env.getProperty("git.templatename." + projectType.toString());
	}
}
