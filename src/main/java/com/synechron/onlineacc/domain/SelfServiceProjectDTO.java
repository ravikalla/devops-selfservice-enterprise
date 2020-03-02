package com.synechron.onlineacc.domain;

import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;

public class SelfServiceProjectDTO {

    private Long userId;
	private String projectName;
	private ProjectType projectType;
	private OrgName orgName;
	private String testURL;

	private String urlJenkinsJob;
	private String urlGitProject;
	private String urlDefectURL;

	public String getUrlJenkinsJob() {
		return urlJenkinsJob;
	}
	public void setUrlJenkinsJob(String urlJenkinsJob) {
		this.urlJenkinsJob = urlJenkinsJob;
	}
	public String getUrlGitProject() {
		return urlGitProject;
	}
	public void setUrlGitProject(String urlGitProject) {
		this.urlGitProject = urlGitProject;
	}
	public String getUrlDefectURL() {
		return urlDefectURL;
	}
	public void setUrlDefectURL(String urlDefectURL) {
		this.urlDefectURL = urlDefectURL;
	}
	public SelfServiceProjectDTO() {
		super();
	}
	public SelfServiceProjectDTO(SelfServiceProjectEntity selfServiceProjectEntity, String urlJenkinsJob, String urlGitProject, String urlDefectURL) {
		super();
		this.userId = selfServiceProjectEntity.getUserId();
		this.projectName = selfServiceProjectEntity.getProjectName();
		this.projectType = selfServiceProjectEntity.getProjectType();
		this.orgName = selfServiceProjectEntity.getOrgName();
		this.testURL = selfServiceProjectEntity.getTestURL();
		this.urlJenkinsJob = urlJenkinsJob;
		this.urlGitProject = urlGitProject;
		this.urlDefectURL = urlDefectURL;
	}

	public ProjectType getProjectType() {
		return projectType;
	}
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
	public OrgName getOrgName() {
		return orgName;
	}
	public void setOrgName(OrgName orgName) {
		this.orgName = orgName;
	}
	public String getTestURL() {
		return testURL;
	}
	public void setTestURL(String testURL) {
		this.testURL = testURL;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
