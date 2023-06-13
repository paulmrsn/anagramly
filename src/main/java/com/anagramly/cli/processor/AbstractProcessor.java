package com.anagramly.cli.processor;

import com.anagramly.cli.io.reader.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public abstract class AbstractProcessor {
    public void process(String filePath) throws IOException {
        AtomicInteger currentWordLength = new AtomicInteger(-1);
        List<String> wordsToProcess = new ArrayList<>();
        try (Stream<String> lines = Reader.read(filePath)) {
            lines.forEach(word -> {
                if(currentWordLength.get() != word.length()) {
                    if(wordsToProcess.size() > 0) {
                        consume(new ArrayList<>(wordsToProcess));
                        wordsToProcess.clear();
                    }
                    currentWordLength.set(word.length());
                }
                wordsToProcess.add(word);
            });
            consume(new ArrayList<>(wordsToProcess));
        }
    }

    abstract void consume(List<String> input);
}
