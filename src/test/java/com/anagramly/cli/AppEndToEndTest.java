package com.anagramly.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppEndToEndTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private File tempFile;

  @Before
  public void setup() throws IOException {
    System.setOut(new PrintStream(outContent));
    tempFile =
        createTempFileWithContent(
            List.of("rat", "art", "tar", "star", "rats", "tars", "bar", "bra", "sart"));
  }

  @After
  public void restore() {
    System.setOut(originalOut);
  }

  @Test
  public void testEndToEndLinear() {
    int exitCode = new CommandLine(new App()).execute("-f", tempFile.getAbsolutePath());
    assertEquals(0, exitCode);

    String[] lines = outContent.toString().split("\n");

    assertEquals(6, lines.length);
    assertEquals("⏰ Processing file -> " + tempFile.getAbsolutePath(), lines[0]);
    assertTrue(lines[lines.length - 1].startsWith("✅ Success -> "));
  }

  @Test
  public void testEndToEndParallel() {
    int exitCode = new CommandLine(new App()).execute("-f", tempFile.getAbsolutePath(), "-p");
    assertEquals(0, exitCode);

    String[] lines = outContent.toString().split("\n");

    assertEquals(6, lines.length);
    assertEquals("⏰ Processing file -> " + tempFile.getAbsolutePath(), lines[0]);
    assertTrue(lines[lines.length - 1].startsWith("✅ Success -> "));
  }

  @Test
  public void testEndToEndOutputToFile() throws IOException {
    File outputFile = File.createTempFile("output", ".txt");
    int exitCode =
        new CommandLine(new App())
            .execute("-f", tempFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath());
    assertEquals(0, exitCode);

    List<String> lines = Files.readAllLines(outputFile.toPath());
    assertEquals(4, lines.size());

    outputFile.delete();
  }

  private File createTempFileWithContent(List<String> lines) throws IOException {
    Path tempFilePath = Files.createTempFile("anagram", "txt");
    Files.write(tempFilePath, lines);
    return tempFilePath.toFile();
  }
}
