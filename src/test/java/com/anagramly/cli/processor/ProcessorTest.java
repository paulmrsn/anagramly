package com.anagramly.cli.processor;

import com.anagramly.cli.AnagramService;
import com.anagramly.cli.io.reader.Reader;
import com.anagramly.cli.io.writer.Writer;
import org.junit.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ProcessorTest {
  private static final String TEST_FILE = "test.txt";
  private static final Collection<List<String>> TEST_ANAGRAMS_GROUPS =
      Arrays.asList(
          Arrays.asList("art", "rat", "tar"),
          Arrays.asList("bar", "bra"),
          Arrays.asList("rats", "sart", "star", "tars"));
  private static final List<String> TEST_ANAGRAMS =
      Arrays.asList("art", "rat", "tar", "bar", "bra", "rats", "sart", "star", "tars");

  private static final String FORMATTED_ANAGRAMS =
      "art,rat,tar" + "\n" + "bar,bra" + "\n" + "rats,sart,start,tars";

  private final List<String> FILE_LINES = Arrays.asList("apple", "papel", "maple", "peach", "cheap", "banana", "anabana");


  @Test
  public void testLinearProcessor() throws IOException {
    Writer writerMock = Mockito.mock(Writer.class);
    Reader readerMock = Mockito.mock(Reader.class);
    AnagramService anagramServiceMock = Mockito.mock(AnagramService.class);

    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS)).thenReturn(TEST_ANAGRAMS_GROUPS);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS))
        .thenReturn(FORMATTED_ANAGRAMS);
    Mockito.when(readerMock.read(TEST_FILE)).thenReturn(FILE_LINES.stream());


    LinearProcessor processor = new LinearProcessor(anagramServiceMock, readerMock, writerMock);
    processor.process(TEST_FILE);

    //    Mockito.verify(writerMock, Mockito.times(1)).write(String.join(", ", TEST_ANAGRAMS));
  }

  @Test
  public void testParallelProcessor() throws IOException, InterruptedException {
    Writer writerMock = Mockito.mock(Writer.class);
    Reader readerMock = Mockito.mock(Reader.class);
    AnagramService anagramServiceMock = Mockito.mock(AnagramService.class);

    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS)).thenReturn(TEST_ANAGRAMS_GROUPS);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS))
        .thenReturn(FORMATTED_ANAGRAMS);

    ParallelProcessor processor = new ParallelProcessor(anagramServiceMock, readerMock, writerMock);
    processor.process(TEST_FILE);
    processor.shutDown();

    //    Mockito.verify(writerMock, Mockito.times(1)).write(String.join(", ", TEST_ANAGRAMS));
  }
}
