package com.anagramly.cli.io.writer;

public class ConsoleWriter implements Writer {
  public void write(String value) {
    System.out.println(value);
  }
}
