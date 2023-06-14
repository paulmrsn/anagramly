package com.anagramly.cli.processor;

import com.anagramly.cli.AnagramService;
import com.anagramly.cli.io.reader.Reader;
import com.anagramly.cli.io.writer.Writer;

import java.util.List;

public class LinearProcessor extends AbstractProcessor {
  private final Writer writer;
  private final AnagramService anagramService;

  public LinearProcessor(AnagramService anagramService, Reader reader, Writer writer) {
    super(reader);
    this.writer = writer;
    this.anagramService = anagramService;
  }

  @Override
  protected void consume(List<String> input) {
    if (isValid(input)) {
      writer.write(anagramService.formatGroup(anagramService.checkAnagrams(input)));
    }
  }
}
