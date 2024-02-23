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
    NetworkPolicyValue previous = yamlRepository.load(
        cacheConfig.getIndividualValuesLocation(systemCode, namespace),
        NetworkPolicyValue.class);
    ListUtils.mergeList(previous.getIngress().getFrom(),
        networkPolicyValue.getIngress().getFrom());
    ListUtils.mergeList(previous.getIngress().getPorts(),
        networkPolicyValue.getIngress().getPorts());
    ListUtils.mergeList(previous.getEgress().getTo(),
        networkPolicyValue.getEgress().getTo());
    ListUtils.mergeList(previous.getEgress().getPorts(),
        networkPolicyValue.getEgress().getPorts());
    yamlRepository.save(
        cacheConfig.getIndividualValuesLocation(systemCode, namespace),
        previous);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Add network policy for " + namespace);
  }

  public void deleteNetworkPolicyTemplate(
      String systemCode, String namespace,
      NetworkPolicyValue networkPolicyValue)
      throws IOException, GitAPIException {
    String cacheLocation = cacheConfig.getIndividualTemplateLocation(systemCode, namespace);
    NetworkPolicyValue previous = yamlRepository.load(cacheLocation, NetworkPolicyValue.class);
    ListUtils.removeList(previous.getIngress().getFrom(),
        networkPolicyValue.getIngress().getFrom());
    ListUtils.removeList(previous.getIngress().getPorts(),
        networkPolicyValue.getIngress().getPorts());
    ListUtils.removeList(previous.getEgress().getTo(),
        networkPolicyValue.getEgress().getTo());
    ListUtils.removeList(previous.getEgress().getPorts(),
        networkPolicyValue.getEgress().getPorts());
    yamlRepository.save(cacheLocation, previous);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Delete network policy for " + namespace);
  }
}
