package com.anagramly.cli.io.writer;

import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.*;

public class FileWriter implements Writer {
  private final Path path;

  public FileWriter(String filename) {
    path = Paths.get(filename);
    try {
      Files.createFile(path); // Create the file if it doesn't exist.
    } catch (FileAlreadyExistsException e) {
      Logger.error(e, "File already exists: {}", path);
      throw new RuntimeException("File already exists: " + path);
    } catch (IOException e) {
      Logger.error(e, "Unable to create file: {}", filename);
      throw new RuntimeException("Unable to create file", e);
    }
  }

  @Override
  public synchronized void write(String value) {
    try {
      Files.write(path, (value + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException("Unable to write to file", e);
    }
  }
}
