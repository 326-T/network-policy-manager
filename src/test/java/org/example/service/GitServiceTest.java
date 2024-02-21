package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitServiceTest {

  @Autowired
  private GitService gitService;

  @Nested
  class Init {
    @Nested
    @DisplayName("正常系")
    class Regular {
      @Test
      @DisplayName("リポジトリを初期化できること")
      void case1() throws GitAPIException, IOException {
        // when
        gitService.init("target/repo");
        // then
        File repoDir = new File("target/repo");
        assertTrue(repoDir.exists());
      }
    }
  }
}