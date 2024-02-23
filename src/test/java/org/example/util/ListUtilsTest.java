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
        List<String> actual = ListUtils.mergeList(previous, current);
        // then
        assertThat(actual).containsExactly("a", "b", "c", "d");
      }

      @Test
      @DisplayName("前がnullの場合でもリストをマージできること")
      void case2() {
        // given
        List<String> previous = null;
        List<String> current = List.of("b", "c", "d");
        // when
        List<String> actual = ListUtils.mergeList(previous, current);
        // then
        assertThat(actual).containsExactly("b", "c", "d");
      }

      @Test
      @DisplayName("後がnullの場合でもリストをマージできること")
      void case3() {
        // given
        List<String> previous = new ArrayList<>(List.of("a", "b"));
        List<String> current = null;
        // when
        List<String> actual = ListUtils.mergeList(previous, current);
        // then
        assertThat(actual).containsExactly("a", "b");
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
        List<String> actual = ListUtils.removeList(previous, current);
        // then
        assertThat(actual).containsExactly("c");
      }

      @Test
      @DisplayName("前がnullの場合でもリストを削除できること")
      void case2() {
        // given
        List<String> previous = null;
        List<String> current = List.of("a", "b", "d");
        // when
        List<String> actual = ListUtils.removeList(previous, current);
        // then
        assertThat(actual).isNull();
      }

      @Test
      @DisplayName("後がnullの場合でもリストを削除できること")
      void case3() {
        // given
        List<String> previous = new ArrayList<>(List.of("a", "b", "c"));
        List<String> current = null;
        // when
        List<String> actual = ListUtils.removeList(previous, current);
        // then
        assertThat(actual).containsExactly("a", "b", "c");
      }
    }
  }
}