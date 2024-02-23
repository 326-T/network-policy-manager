package org.example.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

  private ListUtils() {
  }

  public static <T> List<T> mergeList(List<T> previous, List<T> current) {
    List<T> result = new ArrayList<>();
    if (previous != null) {
      result.addAll(previous);
    }
    if (current != null) {
      current.forEach(item -> {
        if (!result.contains(item)) {
          result.add(item);
        }
      });
    }
    return result;
  }

  public static <T> List<T> removeList(List<T> previous, List<T> current) {
    if (previous == null || current == null) {
      return previous;
    }
    List<T> result = new ArrayList<>(previous);
    current.forEach(item -> result.removeIf(item::equals));
    return result;
  }
}
