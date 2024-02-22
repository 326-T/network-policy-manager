package org.example.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ListUtilsTest {

  @Nested
  class MergeList {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @Test
      @DisplayName("リストをマージできること")
      void case1() {
        // given
        List<String> previous = new ArrayList<>(List.of("a", "b"));
        List<String> current = List.of("b", "c", "d");
        // when
        ListUtils.mergeList(previous, current);
        // then
        assertThat(previous).containsExactly("a", "b", "c", "d");
      }
    }
  }

  @Nested
  class RemoveList {

    @Nested
    @DisplayName("正常系")
    class Regular {

      @Test
      @DisplayName("リストを削除できること")
      void case1() {
        // given
        List<String> previous = new ArrayList<>(List.of("a", "b", "c"));
        List<String> current = List.of("a", "b", "d");
        // when
        ListUtils.removeList(previous, current);
        // then
        assertThat(previous).containsExactly("c");
      }
    }
  }
}