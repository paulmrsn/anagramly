package com.anagramly.cli;

import com.anagramly.cli.processor.AbstractProcessor;
import com.anagramly.cli.processor.LinearProcessor;
import com.anagramly.cli.processor.ParallelProcessor;
import com.anagramly.cli.io.writer.ConsoleWriter;
import com.anagramly.cli.io.writer.FileWriter;
import com.anagramly.cli.io.writer.Writer;
import com.github.lalyos.jfiglet.FigletFont;
import picocli.CommandLine;

import java.io.IOException;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(name = "anagramly")
public class App implements Runnable
{

    @Option(names = {"-f", "--file"}, required = true, description = "The file to process.")
    private String filePath;

    @Option(names = {"-p", "--parallel"}, defaultValue = "false")
    private boolean runInParallel;

    @Option(names = {"-o", "--o"})
    private String outputPath;

    public static void main(String[] args) {
        try {
            System.out.println(FigletFont.convertOneLine("Anagramly"));
        } catch (IOException e) {
            System.out.println("Could not display logo. " + e);
        }
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("⏰ Processing file -> " + filePath);
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
            System.out.println("✅ Success -> " + filePath);
        } catch (Exception e) {
            System.out.println("❌ Something went wrong for: " + filePath + ", err: " + e);
        }
    }
}
