package org.example.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.config.CacheConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheConfigTest {

  @Autowired
  private CacheConfig cacheConfig;

  @Nested
  class GetReadmeLocation {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @DisplayName("README.mdのパスを取得できること")
      @Test
      void case1() {
        // when
        String actual = cacheConfig.getReadmeLocation("systemCode");
        // then
        assertThat(actual).isEqualTo("cache/systemCode/README.md");
      }
    }
  }

  @Nested
  class GetIndividualTemplateLocation {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @DisplayName("ネットワークポリシーテンプレートのパスを取得できること")
      @Test
      void case1() {
        // when
        String actual = cacheConfig.getIndividualTemplateLocation("systemCode", "namespace");
        // then
        assertThat(actual).isEqualTo("cache/systemCode/namespace/templates");
      }
    }
  }

  @Nested
  class GetIndividualValuesLocation {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @DisplayName("ネットワークポリシーのvalues.yamlのパスを取得できること")
      @Test
      void case1() {
        // when
        String actual = cacheConfig.getIndividualValuesLocation("systemCode", "namespace");
        // then
        assertThat(actual).isEqualTo("cache/systemCode/namespace/values.yaml");
      }
    }
  }
}