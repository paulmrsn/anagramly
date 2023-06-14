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

    String content = Files.readString(Paths.get(TEST_FILE));
    assertTrue(content.contains(TEST_VALUE));
  }

  @Test
  public void testConsoleWriter() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    Writer consoleWriter = new ConsoleWriter();
    consoleWriter.write(TEST_VALUE);

    assertTrue(outContent.toString().contains(TEST_VALUE));

    System.setOut(originalOut);
  }

  @Test
  public void testFileWriterWithEmptyString() throws IOException {
    Writer fileWriter = new FileWriter(TEST_FILE);
    fileWriter.write("");

    String content = Files.readString(Paths.get(TEST_FILE));
    assertEquals("" + System.lineSeparator(), content);
  }

  @Test
  public void testFileWriterWithExistingFile() throws IOException {
    String existingContent = "Existing content";
    Path path = Paths.get(TEST_FILE);
    Files.write(path, existingContent.getBytes());

    Writer fileWriter = new FileWriter(TEST_FILE, true);
    fileWriter.write(TEST_VALUE);

    String content = Files.readString(path);
    assertTrue(content.contains(existingContent));
    assertTrue(content.contains(TEST_VALUE));
  }

  @Test
  public void testFileWriterWithExistingFileNoOverwrite() throws IOException {
    String existingContent = "Existing content";
    Path path = Paths.get(TEST_FILE);
    Files.write(path, existingContent.getBytes());

    assertThrows(Exception.class, () -> new FileWriter(TEST_FILE, false));

    String content = Files.readString(path);
    assertTrue(content.contains(existingContent));
    assertFalse(content.contains(TEST_VALUE));
  }

  @Test
  public void testConsoleWriterWithEmptyString() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    Writer consoleWriter = new ConsoleWriter();
    consoleWriter.write("");

    assertEquals("" + System.lineSeparator(), outContent.toString());

    System.setOut(originalOut);
  }
}
