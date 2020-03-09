package com.synechron.onlineacc.util;

public class Constant {
	public static final String TICKET_ORG_NAME = "ravikalla";
	public static final String TICKET_REPO_NAME = "devops-tickets";
	public static final String TICKET_JOB_CREATE_TITLE = "Create new DevOps workflow in LOB=\"<LOB>\" and Technology=\"<TECHNOLOGY>\"";
	public static final String TICKET_JOB_CREATE_STARTED_LABEL = "Pipeline requested";
	public static final String TICKET_JOB_CREATE_BODY = "Creating a new DEVOPS pipeline in LOB=\"<LOB>\" and Technology=\"<TECHNOLOGY>\"";
	public static final String TICKET_JOB_CREATE_COMPLETED_LABEL = "Pipeline created";

	public static final String JENKINS_URI_JAVA_TEMPLATE = "/job/<TEMPLATE_NAME>/config.xml"; // TODO : Get it from properties file
	public static final String JENKINS_URI_ORG = "/job/<ORG_NAME>"; // TODO : Get it from properties file
	public static final String JENKINS_URI_PROJECT_CREATE = "/createItem?name=<PROJECT_NAME>"; // TODO : Get it from properties file
	public static final String JENKINS_URI_PROJECT_VIEW = "/job/<PROJECT_NAME>"; // TODO : Get it from properties file
	public static final String JENKINS_URI_BUILD_WITH_PARAM = "/buildWithParameters?token=<TOKEN_NAME>&MYSQL_ROOT_PASSWORD=root&DOCKER_USER=<DOCKER_USER>&DOCKER_TOKEN=<DOCKER_TOKEN>"; // TODO : Remove the hardcoding of "MYSQL_ROOT_PASSWORD" parameter
	public static final String JENKINS_URI_CRUMB = "/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)"; // TODO : Get it from properties file

	public static final String GIT_URL = "https://github.com";
	public static final String DOCKER_REPOSITORY_URL = "https://hub.docker.com/r";

	public static final String DEFECT_URL = "https://github.com/ravikalla/devops-tickets/issues";
}
