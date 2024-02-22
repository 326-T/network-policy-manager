package org.example.util;

import java.util.List;

public class ListUtils {

  private ListUtils() {
  }

  public static <T> void mergeList(List<T> previous, List<T> current) {
    current.forEach(item -> {
      if (!previous.contains(item)) {
        previous.add(item);
      }
    });
  }

  public static <T> void removeList(List<T> previous, List<T> current) {
    current.forEach(item -> previous.removeIf(item::equals));
  }
}
