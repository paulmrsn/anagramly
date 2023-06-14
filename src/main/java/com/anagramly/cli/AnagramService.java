package com.anagramly.cli;

import java.util.*;

public class AnagramService {
  public Collection<List<String>> checkAnagrams(final List<String> words) {
    Map<String, List<String>> groups = new HashMap<>();
    for (String word : words) {
      String key = charFrequencyKey(word);
      groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
    }
    return groups.values();
  }

  public static String formatGroup(final Collection<List<String>> groups) {
    StringBuilder output = new StringBuilder();
    int i = 0;
    for (List<String> entry : groups) {
      output.append(String.join(",", entry));
      if (i != groups.size() - 1) {
        output.append("\n");
      }
      i++;
    }
    return output.toString();
  }

  private String charFrequencyKey(final String str) {
    Map<Character, Integer> count = new HashMap<>();
    for (char c : str.toCharArray()) {
      count.put(c, count.getOrDefault(c, 0) + 1);
    }

    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Character, Integer> entry : count.entrySet()) {
      sb.append(entry.getKey());
      sb.append('#');
      sb.append(entry.getValue());
      sb.append('#');
    }

    return sb.toString();
  }
}
