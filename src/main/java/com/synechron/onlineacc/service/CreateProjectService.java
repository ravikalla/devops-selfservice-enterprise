package com.synechron.onlineacc.service;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synechron.onlineacc.dao.SelfServiceProjectRepository;
import com.synechron.onlineacc.domain.SelfServiceProjectEntity;
import com.synechron.onlineacc.domain.User;
import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;

@Service
public class CreateProjectService {
	Logger L = LoggerFactory.getLogger(CreateProjectService.class);

	@Autowired UserService userService;
	@Autowired SelfServiceProjectRepository selfServiceProjectRepository;

	public SelfServiceProjectEntity create(Long id, OrgName newOrgName, ProjectType projectType, String strProjectName, String strTestURL, Principal principal) throws Exception {
		L.info("Start : CreateProjectService.create(...) : id = {}, newOrgName = {}, projectType = {}, strProjectName = {}, strTestURL = {}", id, newOrgName, projectType, strProjectName, strTestURL);
		User user = userService.findByUsername(principal.getName());

		L.info("29 : CreateProjectService.create(...) : UserId = {}", user.getUserId());

		SelfServiceProjectEntity selfServiceProjectEntity = new SelfServiceProjectEntity(id, user.getUserId(), strProjectName, projectType, newOrgName, strTestURL);
		selfServiceProjectEntity = selfServiceProjectRepository.save(selfServiceProjectEntity);
		L.info("End : CreateProjectService.create(...) : id = {}, newOrgName = {}, projectType = {}, strProjectName = {}, strTestURL = {}, (null == selfServiceProjectEntity) = {}", id, newOrgName, projectType, strProjectName, strTestURL, (null == selfServiceProjectEntity));
		return selfServiceProjectEntity;
	}

	public List<SelfServiceProjectEntity> get(Principal principal) throws Exception {
		User user = userService.findByUsername(principal.getName());
		L.info("Start : CreateProjectService.get(...) : UserId = {}", user.getUserId());

		List<SelfServiceProjectEntity> findByUserId = selfServiceProjectRepository.findByUserId(user.getUserId());

		L.info("End : CreateProjectService.get(...) : UserId = {}", user.getUserId());

		return findByUserId;
	}
}
