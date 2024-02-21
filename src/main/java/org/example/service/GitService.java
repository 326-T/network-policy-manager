package org.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class GitService {

  private final ResourceLoader resourceLoader;

  public GitService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public void init(String path) throws GitAPIException, IOException {
    File repoDir = new File(path);
    Resource sourceReadmeResource = resourceLoader.getResource("classpath:templates/README.md");
    Path targetReadmePath = repoDir.toPath().resolve("README.md");
    try (Git git = Git.init().setDirectory(repoDir).call()) {
      Files.copy(sourceReadmeResource.getInputStream(), targetReadmePath, StandardCopyOption.REPLACE_EXISTING);
      // README.md をステージングエリアに追加
      git.add().addFilepattern("README.md").call();
      // コミットを作成
      git.commit().setMessage("First commit with README.md").call();
    }
  }
}
