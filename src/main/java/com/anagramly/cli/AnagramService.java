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

  private static String charFrequencyKey(final String str) {
    int[] count = new int[26];
    for (char c : str.toCharArray()) {
      count[c - 'a']++;
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 26; i++) {
      if (count[i] != 0) {
        sb.append((char) (i + 'a'));
        sb.append(count[i]);
      }
    }

    return sb.toString();
  }
}
