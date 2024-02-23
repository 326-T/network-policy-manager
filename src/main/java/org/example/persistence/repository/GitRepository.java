package org.example.persistence.repository;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.example.config.GitConfig;
import org.springframework.stereotype.Repository;

@Repository
public class GitRepository {

  private final GitConfig gitConfig;

  public GitRepository(GitConfig gitConfig) {
    this.gitConfig = gitConfig;
  }

  public void initRepository(String path, String repoUrl, String commitMessage)
      throws GitAPIException, URISyntaxException {
    try (Git git = Git.init().setDirectory(new File(path)).call()) {
      git.remoteAdd()
          .setName(gitConfig.getRemoteName())
          .setUri(new URIish(repoUrl))
          .call();
      git.add().addFilepattern(".").call();
      git.commit().setMessage(commitMessage).call();
    }
  }

  public void commit(String path, String commitMessage) throws GitAPIException, IOException {
    try (Git git = Git.open(new File(path))) {
      git.add().addFilepattern(".").call();
      git.commit().setMessage(commitMessage).call();
    }
  }

  public void pull(String path) throws GitAPIException, IOException {
    try (Git git = Git.open(new File(path))) {
      git.pull().call();
    }
  }

  public void push(String path) throws GitAPIException, IOException {
    try (Git git = Git.open(new File(path))) {
      git.push()
          .setCredentialsProvider(
              new UsernamePasswordCredentialsProvider(gitConfig.getUser(), gitConfig.getToken()))
          .setRemote(gitConfig.getRemoteName())
          .call();
    }
  }
}
