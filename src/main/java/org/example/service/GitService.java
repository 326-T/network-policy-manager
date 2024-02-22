package org.example.service;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.persistence.entity.NetworkPolicyValue;
import org.example.persistence.repository.DirectoryRepository;
import org.example.persistence.repository.YamlRepository;
import org.example.util.FileNameDefinition;
import org.example.util.ListUtils;
import org.springframework.stereotype.Service;

@Service
public class GitService {

  private final DirectoryRepository directoryRepository;
  private final YamlRepository yamlRepository;

  public GitService(DirectoryRepository directoryRepository,
      YamlRepository yamlRepository) {
    this.directoryRepository = directoryRepository;
    this.yamlRepository = yamlRepository;
  }

  public void initRepository(String systemCode) throws GitAPIException, IOException {
    try (Git git = Git.init().setDirectory(new File(systemCode)).call()) {
      directoryRepository.copyResource(FileNameDefinition.README_LOCATION,
          FileNameDefinition.getReadmeLocation(systemCode));
      git.add().addFilepattern(".").call();
      git.commit().setMessage("First commit with README.md").call();
    }
  }

  public void initNetworkPolicyTemplate(String systemCode, String namespace)
      throws IOException, GitAPIException {
    try (Git git = Git.open(new File(systemCode))) {
      directoryRepository.copyResource(FileNameDefinition.NETWORK_POLICY_TEMPLATE_LOCATION,
          FileNameDefinition.getIndividualTemplateLocation(systemCode, namespace));
      initNetworkPolicyValue(systemCode, namespace);
      git.add().addFilepattern(".").call();
      git.commit().setMessage("Add network policy for " + namespace).call();
    }
  }

  public void addNetworkPolicyValue(
      String systemCode, String namespace,
      NetworkPolicyValue networkPolicyValue)
      throws IOException, GitAPIException {
    try (Git git = Git.open(new File(systemCode))) {
      NetworkPolicyValue previous = yamlRepository.load(
          FileNameDefinition.getIndividualValuesLocation(systemCode, namespace),
          NetworkPolicyValue.class);
      ListUtils.mergeList(previous.getIngress().getFrom(),
          networkPolicyValue.getIngress().getFrom());
      ListUtils.mergeList(previous.getIngress().getPorts(),
          networkPolicyValue.getIngress().getPorts());
      ListUtils.mergeList(previous.getEgress().getTo(), networkPolicyValue.getEgress().getTo());
      ListUtils.mergeList(previous.getEgress().getPorts(),
          networkPolicyValue.getEgress().getPorts());

      yamlRepository.save(FileNameDefinition.getIndividualValuesLocation(systemCode, namespace),
          previous);
      git.add().addFilepattern(".").call();
      git.commit().setMessage("Update network policy value for " + namespace).call();
    }
  }

  public void deleteNetworkPolicyTemplate(
      String systemCode, String namespace,
      NetworkPolicyValue networkPolicyValue)
      throws IOException, GitAPIException {
    try (Git git = Git.open(new File(systemCode))) {
      NetworkPolicyValue previous = yamlRepository.load(
          FileNameDefinition.getIndividualValuesLocation(systemCode, namespace),
          NetworkPolicyValue.class);

      ListUtils.removeList(previous.getIngress().getFrom(),
          networkPolicyValue.getIngress().getFrom());
      ListUtils.removeList(previous.getIngress().getPorts(),
          networkPolicyValue.getIngress().getPorts());
      ListUtils.removeList(previous.getEgress().getTo(), networkPolicyValue.getEgress().getTo());
      ListUtils.removeList(previous.getEgress().getPorts(),
          networkPolicyValue.getEgress().getPorts());

      yamlRepository.save(FileNameDefinition.getIndividualValuesLocation(systemCode, namespace),
          previous);
      git.add().addFilepattern(".").call();
      git.commit().setMessage("Delete network policy for " + namespace).call();
    }
  }

  public void deleteNetworkPolicyValue(String systemCode, String namespace)
      throws IOException, GitAPIException {
    try (Git git = Git.open(new File(systemCode))) {
      git.add().addFilepattern(".").call();
      git.commit().setMessage("Delete network policy value for " + namespace).call();
    }
  }

  public void push(String systemCode) throws GitAPIException, IOException {
    try (Git git = Git.open(new File(systemCode))) {
      git.push().call();
    }
  }

  private void initNetworkPolicyValue(String systemCode, String namespace) throws IOException {
    NetworkPolicyValue networkPolicyValue = new NetworkPolicyValue();
    networkPolicyValue.setName("%s-network-policy".formatted(namespace));
    networkPolicyValue.setNamespace(namespace);
    yamlRepository.save(FileNameDefinition.getIndividualValuesLocation(systemCode, namespace),
        networkPolicyValue);
  }
}
