package org.example.service;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.client.ArgoCdClient;
import org.example.client.request.ArgoCdAppRequest;
import org.example.config.ArgoCdConfig;
import org.example.config.CacheConfig;
import org.example.config.GitConfig;
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
  private final ArgoCdClient argoCdClient;
  private final CacheConfig cacheConfig;
  private final GitConfig gitConfig;
  private final ArgoCdConfig argoCdConfig;

  public HelmTemplateService(DirectoryRepository directoryRepository, YamlRepository yamlRepository,
      GitRepository gitRepository, ArgoCdClient argoCdClient, CacheConfig cacheConfig,
      GitConfig gitConfig, ArgoCdConfig argoCdConfig) {
    this.directoryRepository = directoryRepository;
    this.yamlRepository = yamlRepository;
    this.gitRepository = gitRepository;
    this.argoCdClient = argoCdClient;
    this.cacheConfig = cacheConfig;
    this.gitConfig = gitConfig;
    this.argoCdConfig = argoCdConfig;
  }

  public void initNetworkPolicyTemplate(String systemCode, String namespace)
      throws IOException, GitAPIException {
    copyHelmChart(
        CacheConfig.NETWORK_POLICY_TEMPLATE_LOCATION,
        cacheConfig.getIndividualTemplateLocation(systemCode, namespace));
    initNetworkPolicyValue(systemCode, namespace);
    gitRepository.commit(
        cacheConfig.getRepoLocation(systemCode),
        "Add network policy template for " + namespace);
    gitRepository.push(cacheConfig.getRepoLocation(systemCode));
    createArgoCdApp(systemCode, namespace);
  }

  private void copyHelmChart(String srcDir, String destDir) throws IOException {
    directoryRepository.copyResource(
        srcDir + "Chart.yaml",
        destDir + "Chart.yaml");
    directoryRepository.copyResource(
        srcDir + "README.md",
        destDir + "README.md");
    directoryRepository.copyResource(
        srcDir + "/templates/network-policy.yaml",
        destDir + "/templates/network-policy.yaml");
  }

  private void initNetworkPolicyValue(String systemCode, String namespace) throws IOException {
    NetworkPolicyValue networkPolicyValue = new NetworkPolicyValue();
    networkPolicyValue.setName("%s-network-policy".formatted(namespace));
    networkPolicyValue.setNamespace(namespace);
    yamlRepository.save(
        cacheConfig.getIndividualValuesLocation(systemCode, namespace),
        networkPolicyValue);
  }

  private void createArgoCdApp(String systemCode, String namespace) throws IOException {
    ArgoCdAppRequest app = yamlRepository.loadFromResource(
        CacheConfig.ARGOCD_APP_LOCATION,
        ArgoCdAppRequest.class);
    app.getMetadata().setName("network-policy-%s".formatted(namespace));
    app.getMetadata().getLabels().put("systemCode", systemCode);
    app.getSpec().setProject(argoCdConfig.getProject());
    app.getSpec().getSource().setRepoURL(gitConfig.getRepoUrl(systemCode));
    app.getSpec().getSource().setPath(namespace);
    app.getSpec().getDestination().setNamespace(namespace);
    argoCdClient.createApplication(app);
  }
}
