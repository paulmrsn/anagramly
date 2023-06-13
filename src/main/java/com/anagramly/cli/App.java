package com.anagramly.cli;

import com.anagramly.cli.processor.AbstractProcessor;
import com.anagramly.cli.processor.LinearProcessor;
import com.anagramly.cli.processor.ParallelProcessor;
import com.anagramly.cli.io.writer.ConsoleWriter;
import com.anagramly.cli.io.writer.FileWriter;
import com.anagramly.cli.io.writer.Writer;
import picocli.CommandLine;
import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "anagramly")
public class App implements Runnable
{

    @Option(names = {"-f", "--file"}, required = true, description = "The file to process.")
    private String filePath;

    @Option(names = {"-p", "--parallel"}, required = false, defaultValue = "false")
    private boolean runInParallel;

    @Option(names = {"-o", "--o"}, required = false)
    private String outputPath;

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        System.out.println("Processing file: " + filePath);
        try {
            Writer writer;
            if(outputPath != null) {
                writer = new FileWriter(outputPath);
            } else {
                writer = new ConsoleWriter();
            }
            AbstractProcessor processor;
            if(runInParallel) {
                processor = new ParallelProcessor(writer);
                processor.process(filePath);
                ((ParallelProcessor)processor).shutDown();
            } else {
                processor = new LinearProcessor(writer);
                processor.process(filePath);
            }
            System.out.println("✔ Success: " + filePath);
        } catch (Exception e) {
            System.out.println("Something went wrong for: " + filePath + ", err: " + e);
        }
    }
}
