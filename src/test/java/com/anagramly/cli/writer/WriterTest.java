package com.anagramly.cli.writer;

import com.anagramly.cli.io.writer.ConsoleWriter;
import com.anagramly.cli.io.writer.FileWriter;
import com.anagramly.cli.io.writer.Writer;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class WriterTest {
  private static final String TEST_FILE = "test.txt";
  private static final String TEST_VALUE = "Hello, Anagramly!";

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
  public void testFileWriter() throws IOException {
    Writer fileWriter = new FileWriter(TEST_FILE);
    fileWriter.write(TEST_VALUE);

    // Assert that the file contains the test value
    String content = Files.readString(Paths.get(TEST_FILE));
    assertTrue(content.contains(TEST_VALUE));
  }

  @Test
  public void testConsoleWriter() {
    // Capture System.out content
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    Writer consoleWriter = new ConsoleWriter();
    consoleWriter.write(TEST_VALUE);

    // Assert that the correct content was printed
    assertTrue(outContent.toString().contains(TEST_VALUE));

    // Reset System.out to its original
    System.setOut(originalOut);
  }

  @Test
  public void testFileWriterWithEmptyString() throws IOException {
    Writer fileWriter = new FileWriter(TEST_FILE);
    fileWriter.write("");

    // Assert that the file contains the empty string
    String content = Files.readString(Paths.get(TEST_FILE));
    assertEquals("" + System.lineSeparator(), content);
  }

  @Test
  public void testFileWriterWithExistingFile() throws IOException {
    // Create a file with some content
    String existingContent = "Existing content";
    Path path = Paths.get(TEST_FILE);
    Files.write(path, existingContent.getBytes());

    // Write to the existing file
    Writer fileWriter = new FileWriter(TEST_FILE, true);
    fileWriter.write(TEST_VALUE);

    // Assert that the file contains both the existing content and the new content
    String content = Files.readString(path);
    assertTrue(content.contains(existingContent));
    assertTrue(content.contains(TEST_VALUE));
  }

  @Test
  public void testFileWriterWithExistingFileNoOverwrite() throws IOException {
    // Create a file with some content
    String existingContent = "Existing content";
    Path path = Paths.get(TEST_FILE);
    Files.write(path, existingContent.getBytes());

    // Attempt to initialize a writer with writeExistingFile set to false
    assertThrows(Exception.class, () -> new FileWriter(TEST_FILE, false));

    // Assert that the file contains only the existing content and no new content
    String content = Files.readString(path);
    assertTrue(content.contains(existingContent));
    assertFalse(content.contains(TEST_VALUE));
  }

  @Test
  public void testConsoleWriterWithEmptyString() {
    // Capture System.out content
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    Writer consoleWriter = new ConsoleWriter();
    consoleWriter.write("");

    // Assert that an empty string was printed (plus a newline character)
    assertEquals("" + System.lineSeparator(), outContent.toString());

    // Reset System.out to its original
    System.setOut(originalOut);
  }
}
