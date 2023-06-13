package com.anagramly.cli.io.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Reader {
    public static Stream<String> read(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath));
    }
}
