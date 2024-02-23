package org.example.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.service.RepoService;
import org.example.web.request.CreateRepoRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/repos")
public class RepoController {

  private final RepoService repoService;

  public RepoController(RepoService repoService) {
    this.repoService = repoService;
  }

  @PostMapping
  public void createSystemRepository(@RequestBody CreateRepoRequest request)
      throws GitAPIException, IOException, URISyntaxException {
    repoService.initRepository(request.getSystemCode());
  }
}
