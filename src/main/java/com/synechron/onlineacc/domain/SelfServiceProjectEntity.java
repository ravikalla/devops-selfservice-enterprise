package com.synechron.onlineacc.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;

@Entity
public class SelfServiceProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
	private String projectName;
	@Enumerated(EnumType.STRING)
	private ProjectType projectType;
	@Enumerated(EnumType.STRING)
	private OrgName orgName;
	private String testURL;

	public SelfServiceProjectEntity(Long id, Long userId, String projectName, ProjectType projectType, OrgName orgName, String testURL) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.projectType = projectType;
		this.orgName = orgName;
		this.testURL = testURL;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
