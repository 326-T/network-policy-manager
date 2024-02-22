package org.example.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

@SpringBootTest
class DirectoryRepositoryTest {

  @Autowired
  private DirectoryRepository directoryRepository;

  @AfterEach
  void tearDown() {
    FileSystemUtils.deleteRecursively(new File("cache/DirectoryRepositoryTest"));
  }

  @Nested
  class CopyResource {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @Test
      @DisplayName("リソースをコピーできること")
      void case1() throws IOException {
        // when
        Long size = directoryRepository.copyResource("classpath:templates/README.md",
            "cache/DirectoryRepositoryTest/README.md");
        // then
        File file = new File("cache/DirectoryRepositoryTest/README.md");
        assertThat(file).exists().hasSize(size);
      }
    }
  }
}