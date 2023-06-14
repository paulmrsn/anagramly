package com.anagramly.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnagramServiceTest {
  private List<String> words;
  private final AnagramService anagramService = new AnagramService();

  @Before
  public void setup() {
    words = Arrays.asList("rat", "art", "tar", "star", "rats", "tars", "bar", "bra", "sart");
  }

  @Test
  public void testCheckAnagrams() {
    Collection<List<String>> anagramGroups = anagramService.checkAnagrams(words);
    assertEquals(3, anagramGroups.size());

    boolean foundRatGroup = false;
    boolean foundBarGroup = false;
    boolean foundStarGroup = false;

    for (List<String> group : anagramGroups) {
      Collections.sort(group);
      if (group.equals(Arrays.asList("art", "rat", "tar"))) {
        foundRatGroup = true;
      } else if (group.equals(Arrays.asList("bar", "bra"))) {
        foundBarGroup = true;
      } else if (group.equals(Arrays.asList("rats", "sart", "star", "tars"))) {
        foundStarGroup = true;
      }
    }

    assertTrue(foundRatGroup);
    assertTrue(foundBarGroup);
    assertTrue(foundStarGroup);
  }

  @Test
  public void testCheckAnagramsNonAnagrams() {
    List<String> nonAnagrams = Arrays.asList("abc", "def", "ghi");
    Collection<List<String>> anagramGroups = anagramService.checkAnagrams(nonAnagrams);
    assertEquals(3, anagramGroups.size());

    for (List<String> group : anagramGroups) {
      assertEquals(1, group.size());
    }
  }

  @Test
  public void testCheckAnagramsEmptyString() {
    List<String> wordsWithEmpty = Arrays.asList("", "abc");
    Collection<List<String>> anagramGroups = anagramService.checkAnagrams(wordsWithEmpty);
    assertEquals(2, anagramGroups.size());

    boolean foundEmptyGroup = false;
    for (List<String> group : anagramGroups) {
      if (group.contains("")) {
        foundEmptyGroup = true;
        break;
      }
    }

    assertTrue(foundEmptyGroup);
  }

  @Test
  public void testCheckAnagramsEmptyList() {
    List<String> emptyList = new ArrayList<>();
    Collection<List<String>> anagramGroups = anagramService.checkAnagrams(emptyList);
    assertTrue(anagramGroups.isEmpty());
  }

  @Test
  public void testCheckAnagramsNull() {
    Collection<List<String>> anagramGroups = anagramService.checkAnagrams(null);
    assertTrue(anagramGroups.isEmpty());
  }

  @Test
  public void testFormatGroup() {
    Collection<List<String>> groups = new ArrayList<>();
    groups.add(Arrays.asList("art", "rat", "tar"));
    groups.add(Arrays.asList("bar", "bra"));
    groups.add(Arrays.asList("rats", "sart", "star", "tars"));

    String output = anagramService.formatGroup(groups);

    assertTrue(output.contains("art,rat,tar"));
    assertTrue(output.contains("bar,bra"));
    assertTrue(output.contains("rats,sart,star,tars"));
    assertEquals(3, output.split("\n").length);
  }

  @Test
  public void testFormatGroupOneElement() {
    Collection<List<String>> groups = new ArrayList<>();
    groups.add(List.of("abc"));
    String output = anagramService.formatGroup(groups);

    assertEquals("abc", output);
  }

  @Test
  public void testFormatGroupNoElement() {
    Collection<List<String>> groups = new ArrayList<>();
    String output = anagramService.formatGroup(groups);

    assertEquals("", output);
  }

  @Test
  public void testFormatGroupNull() {
    String output = anagramService.formatGroup(null);

    assertEquals("", output);
  }
}
