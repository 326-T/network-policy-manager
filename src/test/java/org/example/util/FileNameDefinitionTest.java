package org.example.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileNameDefinitionTest {

  @Nested
  class GetReadmeLocation {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @DisplayName("README.mdのパスを取得できること")
      @Test
      void case1() {
        // when
        String actual = FileNameDefinition.getReadmeLocation("systemCode");
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
        String actual = FileNameDefinition.getIndividualTemplateLocation("systemCode", "namespace");
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
        String actual = FileNameDefinition.getIndividualValuesLocation("systemCode", "namespace");
        // then
        assertThat(actual).isEqualTo("cache/systemCode/namespace/values.yaml");
      }
    }
  }
}