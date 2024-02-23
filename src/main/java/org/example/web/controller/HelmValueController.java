package org.example.web.controller;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.service.HelmValueService;
import org.example.web.mapper.NetworkPolicyValueMapper;
import org.example.web.request.HelmValueRequest;
import org.example.web.request.NetworkPolicyValueRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/helm-values")
public class HelmValueController {

  private final HelmValueService helmValueService;
  private final NetworkPolicyValueMapper networkPolicyValueMapper;

  public HelmValueController(HelmValueService helmValueService,
      NetworkPolicyValueMapper networkPolicyValueMapper) {
    this.helmValueService = helmValueService;
    this.networkPolicyValueMapper = networkPolicyValueMapper;
  }

  @PutMapping
  public void addNetworkPolicyValue(
      @RequestBody HelmValueRequest<NetworkPolicyValueRequest> helmValueRequest)
      throws GitAPIException, IOException {
    helmValueService.addNetworkPolicyValue(
        helmValueRequest.getSystemCode(),
        helmValueRequest.getNamespace(),
        networkPolicyValueMapper.mapToEntity(helmValueRequest.getBody()));
  }

  @DeleteMapping
  public void deleteNetworkPolicyValue(
      @RequestBody HelmValueRequest<NetworkPolicyValueRequest> helmValueRequest)
      throws GitAPIException, IOException {
    helmValueService.deleteNetworkPolicyValue(
        helmValueRequest.getSystemCode(),
        helmValueRequest.getNamespace(),
        networkPolicyValueMapper.mapToEntity(helmValueRequest.getBody()));
  }
}
