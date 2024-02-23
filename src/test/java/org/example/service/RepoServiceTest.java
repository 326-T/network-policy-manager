package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RepoServiceTest {

  @Autowired
  private RepoService repoService;

  @Nested
  class Init {
    @Nested
    @DisplayName("正常系")
    class Regular {
      @Test
      @DisplayName("リポジトリを初期化できること")
      void case1() throws GitAPIException, IOException, URISyntaxException {
        // when
        repoService.initRepository("target/repo");
        // then
        File repoDir = new File("target/repo/README.md");
        assertTrue(repoDir.exists());
      }
    }
  }
//
//  @Nested
//  class AddNetworkPolicyTemplate {
//    @Nested
//    @DisplayName("正常系")
//    class Regular {
//      @Test
//      @DisplayName("ネットワークポリシーテンプレートを追加できること")
//      void case1() throws GitAPIException, IOException {
//        // given
//        gitService.initRepository("target/repo");
//        // when
//        gitService.initNetworkPolicyTemplate("target/repo", "namespace1");
//        // then
//        File networkPolicyDir = new File("target/repo/namespace1");
//        assertTrue(networkPolicyDir.exists());
//      }
//    }
//  }
}