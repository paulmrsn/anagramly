package com.anagramly.cli.processor;

import com.anagramly.cli.Anagram;
import com.anagramly.cli.io.writer.ConsoleWriter;
import com.anagramly.cli.io.writer.Writer;

import java.util.List;

public class LinearProcessor extends AbstractProcessor {
    private final Writer writer;
    public LinearProcessor() {
        this(new ConsoleWriter());
    }
    public LinearProcessor(Writer writer) {
        this.writer = writer;
    }
    @Override
    protected void consume(List<String> input) {
        writer.write(Anagram.formatGroup(Anagram.checkAnagrams(input)));
    }
}
