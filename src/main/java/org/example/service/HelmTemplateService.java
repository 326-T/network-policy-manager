package org.example.service;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.config.CacheConfig;
import org.example.persistence.entity.NetworkPolicyValue;
import org.example.persistence.repository.DirectoryRepository;
import org.example.persistence.repository.GitRepository;
import org.example.persistence.repository.YamlRepository;
import org.springframework.stereotype.Service;

@Service
public class HelmTemplateService {

  private final DirectoryRepository directoryRepository;
  private final YamlRepository yamlRepository;
  private final GitRepository gitRepository;
  private final CacheConfig cacheConfig;

  public HelmTemplateService(DirectoryRepository directoryRepository, YamlRepository yamlRepository,
      GitRepository gitRepository, CacheConfig cacheConfig) {
    this.directoryRepository = directoryRepository;
    this.yamlRepository = yamlRepository;
    this.gitRepository = gitRepository;
    this.cacheConfig = cacheConfig;
  }

  public void initNetworkPolicyTemplate(String systemCode, String namespace)
      throws IOException, GitAPIException {
    directoryRepository.copyResource(
        CacheConfig.NETWORK_POLICY_TEMPLATE_LOCATION,
        cacheConfig.getIndividualTemplateLocation(systemCode, namespace));
    initNetworkPolicyValue(systemCode, namespace);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Add network policy template for " + namespace);
  }

  private void initNetworkPolicyValue(String systemCode, String namespace) throws IOException {
    NetworkPolicyValue networkPolicyValue = new NetworkPolicyValue();
    networkPolicyValue.setName("%s-network-policy".formatted(namespace));
    networkPolicyValue.setNamespace(namespace);
    yamlRepository.save(
        cacheConfig.getIndividualValuesLocation(systemCode, namespace),
        networkPolicyValue);
  }
}
