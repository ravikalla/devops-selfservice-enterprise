package com.synechron.onlineacc.controller;

import static com.synechron.onlineacc.util.AppConstants.URI_ADMIN;
import static com.synechron.onlineacc.util.AppConstants.URI_ADMIN_CREDENTIALS_JENKINS_GIT;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.synechron.onlineacc.util.CustomGlobalContext;

@Controller
@RequestMapping(value = URI_ADMIN)
public class AdminController {
	Logger L = LoggerFactory.getLogger(AdminController.class);

	@RequestMapping(value = URI_ADMIN_CREDENTIALS_JENKINS_GIT, method = RequestMethod.GET)
	public String saveCredentials(Model model) {
		L.debug("Start : AdminController.saveCredentials(...)");

		model.addAttribute("jenkinsURL", "");
		model.addAttribute("jenkinsToken", ""); // TODO : Send a list of technologies from controller instead of hardcoding in the UI (createPipelineForAppsRegular.html)
		model.addAttribute("jenkinsUserName", ""); // TODO : Send a list of organization names from controller instead of hardcoding in the UI (createPipelineForAppsRegular.html)
		model.addAttribute("jenkinsPwd", "");
		model.addAttribute("gitToken", "");
		model.addAttribute("sonarUrl", "");

		L.debug("End : AdminController.saveCredentials(...)");
		return "adminJenkinsGitCredentials";
	}

	@RequestMapping(value = URI_ADMIN_CREDENTIALS_JENKINS_GIT, method = RequestMethod.POST)
	public String setJenkinsCredentials(
			@RequestParam(value = "sonarUrl", required = false) String strSonarUrl
			, @RequestParam(value = "jenkinsURL", required = true) String strJenkinsURL
			, @RequestParam(value = "jenkinsToken", required = false) String strJenkinsToken
			, @RequestParam(value = "jenkinsUserName", required = false) String strJenkinsUserName
			, @RequestParam(value = "jenkinsPwd", required = false) String strJenkinsPwd
			, @RequestParam(value = "gitToken", required = true) String strGitToken
			, @RequestParam(value = "dockerUserName", required = true) String strDockerUserName
			, @RequestParam(value = "dockerToken", required = true) String strDockerToken
			, Principal principal
			)
			throws Exception {
		L.info("Start : AdminController.setJenkinsCredentials(...) : strSonarURL = {}, strJenkinsURL = {} : strJenkinsUserName = {}, strDockerUserName = {}", strSonarUrl, strJenkinsURL, strJenkinsUserName, strDockerUserName);
		CustomGlobalContext.setJenkinsUrl(strJenkinsURL);

		if (null != strSonarUrl)
			CustomGlobalContext.setSonarUrl(strSonarUrl);
		if (null != strJenkinsToken)
			CustomGlobalContext.setJenkinsToken(strJenkinsToken);
		if (null != strJenkinsUserName)
			CustomGlobalContext.setJenkinsUserName(strJenkinsUserName);
		if (null != strJenkinsPwd)
			CustomGlobalContext.setJenkinsPwd(strJenkinsPwd);
		if (null != strGitToken)
			CustomGlobalContext.setGitToken(strGitToken);
		if (null != strDockerUserName)
			CustomGlobalContext.setDockerUserName(strDockerUserName);
		if (null != strDockerToken)
			CustomGlobalContext.setDockerToken(strDockerToken);
		L.info("End : AdminController.setJenkinsCredentials(...) : strSonarURL = {}, strJenkinsURL = {} : strJenkinsUserName = {}, strDockerUserName = {}", strSonarUrl, strJenkinsURL, strJenkinsUserName, strDockerUserName);
		return "redirect:/userFront";
	}
}