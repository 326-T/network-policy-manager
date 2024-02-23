package org.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.config.CacheConfig;
import org.example.persistence.entity.NetworkPolicyValue;
import org.example.persistence.entity.NetworkPolicyValue.From;
import org.example.persistence.entity.NetworkPolicyValue.PortType;
import org.example.persistence.entity.NetworkPolicyValue.To;
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
    mergeIngress(previous, networkPolicyValue);
    mergeEgress(previous, networkPolicyValue);
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
    removeIngress(previous, networkPolicyValue);
    removeEgress(previous, networkPolicyValue);
    yamlRepository.save(cacheLocation, previous);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Delete network policy for " + namespace);
    gitRepository.push(cacheConfig.getRepoLocation(systemCode));
  }

  private void mergeIngress(
      NetworkPolicyValue previous,
      NetworkPolicyValue after) {
    if (Objects.isNull(after.getIngress())) {
      return;
    }
    if (Objects.isNull(previous.getIngress())) {
      previous.setIngress(after.getIngress());
      return;
    }
    List<From> from = ListUtils.mergeList(previous.getIngress().getFrom(),
        after.getIngress().getFrom());
    List<PortType> ports = ListUtils.mergeList(previous.getIngress().getPorts(),
        after.getIngress().getPorts());
    previous.getIngress().setFrom(from);
    previous.getIngress().setPorts(ports);
  }

  private void mergeEgress(
      NetworkPolicyValue previous,
      NetworkPolicyValue after) {
    if (Objects.isNull(after.getEgress())) {
      return;
    }
    if (Objects.isNull(previous.getEgress())) {
      previous.setEgress(after.getEgress());
      return;
    }
    List<To> to = ListUtils.mergeList(
        previous.getEgress().getTo(),
        after.getEgress().getTo());
    List<PortType> ports = ListUtils.mergeList(
        previous.getEgress().getPorts(),
        after.getEgress().getPorts());
    previous.getEgress().setTo(to);
    previous.getEgress().setPorts(ports);
  }

  private void removeIngress(
      NetworkPolicyValue previous,
      NetworkPolicyValue after) {
    if (Objects.isNull(after.getIngress())) {
      return;
    }
    if (Objects.isNull(previous.getIngress())) {
      return;
    }
    List<From> from = ListUtils.removeList(
        previous.getIngress().getFrom(), after.getIngress().getFrom());
    List<PortType> ports = ListUtils.removeList(
        previous.getIngress().getPorts(), after.getIngress().getPorts());
    previous.getIngress().setFrom(from);
    previous.getIngress().setPorts(ports);
  }

  private void removeEgress(
      NetworkPolicyValue previous,
      NetworkPolicyValue after) {
    if (Objects.isNull(after.getEgress())) {
      return;
    }
    if (Objects.isNull(previous.getEgress())) {
      return;
    }
    List<To> to = ListUtils.removeList(
        previous.getEgress().getTo(), after.getEgress().getTo());
    List<PortType> ports = ListUtils.removeList(
        previous.getEgress().getPorts(), after.getEgress().getPorts());
    previous.getEgress().setTo(to);
    previous.getEgress().setPorts(ports);
  }
}
