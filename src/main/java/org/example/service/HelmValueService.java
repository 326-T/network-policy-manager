package org.example.service;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.config.CacheConfig;
import org.example.persistence.entity.NetworkPolicyValue;
import org.example.persistence.repository.GitRepository;
import org.example.persistence.repository.YamlRepository;
import org.example.util.ListUtils;
import org.springframework.stereotype.Service;

@Service
public class HelmValueService {

  private final YamlRepository yamlRepository;
  private final GitRepository gitRepository;
  private final CacheConfig cacheConfig;

  public HelmValueService(YamlRepository yamlRepository, GitRepository gitRepository,
      CacheConfig cacheConfig) {
    this.yamlRepository = yamlRepository;
    this.gitRepository = gitRepository;
    this.cacheConfig = cacheConfig;
  }

  public void addNetworkPolicyValue(
      String systemCode, String namespace,
      NetworkPolicyValue networkPolicyValue)
      throws IOException, GitAPIException {
    String cacheLocation = cacheConfig.getIndividualValuesLocation(systemCode, namespace);
    NetworkPolicyValue previous = yamlRepository.load(cacheLocation,
        NetworkPolicyValue.class);
    previous.setIngress(ListUtils.mergeList(
        previous.getIngress(), networkPolicyValue.getIngress()));
    previous.setEgress(ListUtils.mergeList(
        previous.getEgress(), networkPolicyValue.getEgress()));
    yamlRepository.save(cacheLocation, previous);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Add network policy for " + namespace);
    gitRepository.push(cacheConfig.getRepoLocation(systemCode));
  }

  public void deleteNetworkPolicyValue(
      String systemCode, String namespace,
      NetworkPolicyValue networkPolicyValue)
      throws IOException, GitAPIException {
    String cacheLocation = cacheConfig.getIndividualValuesLocation(systemCode, namespace);
    NetworkPolicyValue previous = yamlRepository.load(cacheLocation, NetworkPolicyValue.class);
    previous.setIngress(ListUtils.removeList(
        previous.getIngress(), networkPolicyValue.getIngress()));
    previous.setEgress(ListUtils.removeList(
        previous.getEgress(), networkPolicyValue.getEgress()));
    yamlRepository.save(cacheLocation, previous);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Delete network policy for " + namespace);
    gitRepository.push(cacheConfig.getRepoLocation(systemCode));
  }
}
