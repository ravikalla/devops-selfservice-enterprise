package com.synechron.onlineacc.service;

import java.io.File;
import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.synechron.onlineacc.util.CustomGlobalContext;
import com.synechron.onlineacc.util.OrgName;
import com.synechron.onlineacc.util.ProjectType;

@Service
public class SourceCodeService {
	Logger L = LoggerFactory.getLogger(SourceCodeService.class);

	@Autowired
	private Environment env;

	public Repository create(String strOrg, String strToken, String strRepoName, Boolean blnIsPrivate, String strCloneURL) throws IOException {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(strToken);
		RepositoryService repositoryService = new RepositoryService(client); // TODO : Optimize this by creating a Spring bean

		Repository repository  = null;
		try {
			repository = createProject(strOrg, strRepoName, blnIsPrivate, strCloneURL, repositoryService);
		} catch (IOException e) {
			L.error("29 : SourceCodeService.create(...) : IOException e = {}", e);
			throw e;
		}
		return repository;
	}

	public Repository gitFork(ProjectType projectType, OrgName newOrgName) {
		L.info("Start : SourceCodeService.gitFork(...) : strNewOrg = {}, strProjectName = {}", projectType, newOrgName);
		String strTemplateOrg = env.getProperty("git.orgname");
		String strTemplateRepoName = env.getProperty("git.templatename." + projectType.toString());

		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(CustomGlobalContext.getGitToken());
		RepositoryService repositoryService = new RepositoryService(client); // TODO : Optimize this by creating a Spring bean

		Repository repository  = null;
		repository = forkProject(strTemplateOrg, strTemplateRepoName, newOrgName, repositoryService);
		L.info("End : SourceCodeService.gitFork(...) : strNewOrg = {}, strProjectName = {}", projectType, newOrgName);
		return repository;
	}

	private Repository createProject(String strOrg, String strRepoName, Boolean blnIsPrivate, String strCloneURL, RepositoryService repositoryService) throws IOException {
		Repository repository = new Repository();
		repository.setName(strRepoName);
		repository.setPrivate(blnIsPrivate);
		repository.setCloneUrl(strCloneURL);
		Repository createRepository = repositoryService.createRepository(strOrg, repository);
		return createRepository;
	}

	private Repository forkProject(String strOrg, String strRepoName, OrgName newOrgName, RepositoryService repositoryService) {
		L.info("Start : SourceCodeService.forkProject(...) : strOrg = {}, strRepoName = {}, newOrgName = {}", strOrg, strRepoName, newOrgName);
		Repository createRepository = null;
		RepositoryId repo = new RepositoryId(strOrg, strRepoName);
		try {
			createRepository = repositoryService.forkRepository(repo, newOrgName.toString());
		} catch (IOException e) {
			L.error("SourceCodeService.forkProject(...) : strOrg = {}, strRepoName = {}, newOrgName = {}, IOException e = {}", strOrg, strRepoName, newOrgName, e);
		}
		L.info("End : SourceCodeService.forkProject(...) : strOrg = {}, strRepoName = {}, newOrgName = {}, (null == createRepository) = {}", strOrg, strRepoName, newOrgName, (null == createRepository));
		return createRepository;
	}

	public void cloneRepo(String owner, String repoName, RepositoryService rs, String LOCAL_TEMP_PATH)
			throws Exception {
		Git result = null;
		try {
			Repository r = rs.getRepository(owner, repoName);

			String cloneURL = r.getSshUrl();
			// prepare a new folder for the cloned repository
			File localPath = new File(LOCAL_TEMP_PATH + File.separator + owner + File.separator + repoName);
			if (localPath.isDirectory() == false) {
				localPath.mkdirs();
			} else {
				throw new Exception("Local directory already exists. Delete it first: " + localPath);
			}

			L.debug("Cloning from " + cloneURL + " to " + localPath);
			result = Git.cloneRepository().setURI(cloneURL).setDirectory(localPath).call();
			// Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
			L.debug("Cloned repository: " + result.getRepository().getDirectory());
		} catch (IOException | GitAPIException ex) {
			throw new Exception("Problem cloning repo: " + ex.getMessage());
		} finally {
			if (result != null) {
				result.close();
			}
		}
	}
}
