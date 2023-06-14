package com.anagramly.cli.reader;

import com.anagramly.cli.io.reader.Reader;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ReaderTest {
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_VALUE = "Hello, Anagramly!";
    private final Reader reader = new Reader();

    @After
    public void cleanUp() {
        // Delete the file after each test
        Path path = Paths.get(TEST_FILE);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadFromFile() throws IOException {
        // Write some content to a file
        Files.write(Paths.get(TEST_FILE), TEST_VALUE.getBytes());

        // Read from the file
        Stream<String> lines = reader.read(TEST_FILE);

        // Assert that the file contains the test value
        assertTrue(lines.anyMatch(line -> line.contains(TEST_VALUE)));
    }

    @Test
    public void testReadFromEmptyFile() throws IOException {
        // Create an empty file
        Files.createFile(Paths.get(TEST_FILE));

        // Read from the file
        Stream<String> lines = reader.read(TEST_FILE);

        // Assert that the file is empty
        assertEquals(0, lines.count());
    }

    @Test
    public void testReadFromNonexistentFile() {
        // Attempt to read from a file that doesn't exist
        assertThrows(NoSuchFileException.class, () -> reader.read("nonexistent.txt"));
    }
}
