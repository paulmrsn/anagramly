package com.anagramly.cli;

import org.tinylog.Logger;

import java.util.*;

public class AnagramService {
  public Collection<List<String>> checkAnagrams(final List<String> words) {
    if (words == null || words.isEmpty()) {
      Logger.warn("formatGroup: Empty of null list passed!");
      return Collections.emptyList();
    }
    Map<String, List<String>> groups = new HashMap<>();
    for (String word : words) {
      String key = charFrequencyKey(word);
      groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
    }
    return groups.values();
  }

  public String formatGroup(final Collection<List<String>> groups) {
    if (groups == null || groups.isEmpty()) {
      Logger.warn("formatGroup: Empty of null group passed!");
      return "";
    }
    StringJoiner groupJoiner = new StringJoiner("\n");
    for (List<String> group : groups) {
      StringJoiner wordJoiner = new StringJoiner(",");
      for (String word : group) {
        wordJoiner.add(word);
      }
      groupJoiner.add(wordJoiner.toString());
    }
    return groupJoiner.toString();
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
