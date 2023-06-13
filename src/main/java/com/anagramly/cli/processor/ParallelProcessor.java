package com.anagramly.cli.processor;

import com.anagramly.cli.Anagram;
import com.anagramly.cli.io.writer.Writer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelProcessor extends AbstractProcessor {
  private static final int N_THREADS = Runtime.getRuntime().availableProcessors();
  private final ExecutorService executorService;
  private final Writer writer;

  public ParallelProcessor(Writer writer) {
    this.executorService = Executors.newFixedThreadPool(N_THREADS);
    this.writer = writer;
  }

  @Override
  protected void consume(List<String> input) {
    executorService.execute(() -> writer.write(Anagram.formatGroup(Anagram.checkAnagrams(input))));
  }

  public void shutDown() throws InterruptedException {
    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
  }
}
