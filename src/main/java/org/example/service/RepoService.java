package org.example.service;

import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.config.CacheConfig;
import org.example.config.GitConfig;
import org.example.persistence.repository.DirectoryRepository;
import org.example.persistence.repository.GitRepository;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

  private final DirectoryRepository directoryRepository;
  private final GitRepository gitRepository;
  private final CacheConfig cacheConfig;
  private final GitConfig gitConfig;

  public RepoService(DirectoryRepository directoryRepository, GitRepository gitRepository,
      CacheConfig cacheConfig, GitConfig gitConfig) {
    this.directoryRepository = directoryRepository;
    this.gitRepository = gitRepository;
    this.cacheConfig = cacheConfig;
    this.gitConfig = gitConfig;
  }

  public void initRepository(String systemCode)
      throws GitAPIException, IOException, URISyntaxException {
    directoryRepository.copyResource(
        CacheConfig.README_LOCATION,
        cacheConfig.getReadmeLocation(systemCode));
    gitRepository.initRepository(
        cacheConfig.getRepoLocation(systemCode),
        gitConfig.getRepoUrl(systemCode),
        "First commit with README.md");
    gitRepository.push(cacheConfig.getRepoLocation(systemCode));
  }
}