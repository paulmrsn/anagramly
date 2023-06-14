package com.anagramly.cli.reader;

import com.anagramly.cli.io.reader.Reader;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ReaderTest {
  private static final String TEST_FILE = "test.txt";
  private static final String TEST_VALUE = "Hello, Anagramly!";
  private final Reader reader = new Reader();

  @After
  public void cleanUp() {
    // Delete the file after each test
    Path path = Paths.get(TEST_FILE);
    try {
      Files.deleteIfExists(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReadFromFile() throws IOException {
    Files.write(Paths.get(TEST_FILE), TEST_VALUE.getBytes());

    Stream<String> lines = reader.read(TEST_FILE);

    assertTrue(lines.anyMatch(line -> line.contains(TEST_VALUE)));
  }

  @Test
  public void testReadFromEmptyFile() throws IOException {
    Files.createFile(Paths.get(TEST_FILE));

    Stream<String> lines = reader.read(TEST_FILE);

    assertEquals(0, lines.count());
  }

  @Test
  public void testReadFromNonexistentFile() {
    assertThrows(NoSuchFileException.class, () -> reader.read("nonexistent.txt"));
  }
}
