package org.example.web.controller;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.service.HelmTemplateService;
import org.example.web.request.CreateHelmChartRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/charts")
public class HelmTemplateController {

  private final HelmTemplateService helmTemplateService;

  public HelmTemplateController(HelmTemplateService helmTemplateService) {
    this.helmTemplateService = helmTemplateService;
  }

  @PostMapping
  public void createHelmTemplate(@RequestBody CreateHelmChartRequest createHelmChartRequest)
      throws GitAPIException, IOException {
    helmTemplateService.initNetworkPolicyTemplate(
        createHelmChartRequest.getSystemCode(),
        createHelmChartRequest.getNamespace());
  }
}
