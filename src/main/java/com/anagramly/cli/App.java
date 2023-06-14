package com.anagramly.cli;

import com.anagramly.cli.io.reader.Reader;
import com.anagramly.cli.processor.AbstractProcessor;
import com.anagramly.cli.processor.LinearProcessor;
import com.anagramly.cli.processor.ParallelProcessor;
import com.anagramly.cli.io.writer.ConsoleWriter;
import com.anagramly.cli.io.writer.FileWriter;
import com.anagramly.cli.io.writer.Writer;
import com.github.lalyos.jfiglet.FigletFont;
import org.tinylog.Logger;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "anagramly")
public class App implements Callable<Integer> {

  @Option(
      names = {"-f", "--file"},
      required = true,
      description = "The file to process.")
  private String filePath;

  @Option(
      names = {"-p", "--parallel"},
      defaultValue = "false")
  private boolean runInParallel;

  @Option(names = {"-o", "--o"})
  private String outputPath;

  public static void main(String[] args) {
    try {
      System.out.println(FigletFont.convertOneLine("Anagramly"));
    } catch (IOException e) {
      Logger.error(e, "Could not display logo. ");
    }
    int exitCode = new CommandLine(new App()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() {
    System.out.println("⏰ Processing file -> " + filePath);
    try {
      AnagramService anagramService = new AnagramService();
      Writer writer = outputPath != null ? new FileWriter(outputPath) : new ConsoleWriter();
      Reader reader = new Reader();
      AbstractProcessor processor;

      if (runInParallel) {
        processor = new ParallelProcessor(anagramService, reader, writer);
        processor.process(filePath);
        ((ParallelProcessor) processor).shutDown();
      } else {
        processor = new LinearProcessor(anagramService, reader, writer);
        processor.process(filePath);
      }
      System.out.println("✅ Success -> " + filePath);
      return 0;
    } catch (Exception e) {
      Logger.error(e);
      System.out.println("❌ Something went wrong for: " + filePath + ", err: " + e);
      return 1;
    }
  }
}
