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
  private static final Collection<List<String>> TEST_ANAGRAMS_GROUPS_1 =
      Arrays.asList(Arrays.asList("art", "rat", "tar"), Arrays.asList("bar", "bra"));
  private static final Collection<List<String>> TEST_ANAGRAMS_GROUPS_2 =
      List.of(Arrays.asList("rats", "sart", "star", "tars"));
  private static final List<String> TEST_ANAGRAMS_1 =
      Arrays.asList("art", "rat", "tar", "bar", "bra");
  private static final List<String> TEST_ANAGRAMS_2 = Arrays.asList("rats", "sart", "star", "tars");

  private static final String FORMATTED_ANAGRAMS_1 = "art,rat,tar" + "\n" + "bar,bra";
  private static final String FORMATTED_ANAGRAMS_2 = "rats,sart,start,tars";
  private final List<String> FILE_LINES =
      Arrays.asList("art", "rat", "tar", "bar", "bra", "rats", "sart", "star", "tars");

  @Test
  public void testLinearProcessor() throws IOException {
    Writer writerMock = Mockito.mock(Writer.class);
    Reader readerMock = Mockito.mock(Reader.class);
    AnagramService anagramServiceMock = Mockito.mock(AnagramService.class);

    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS_1))
        .thenReturn(TEST_ANAGRAMS_GROUPS_1);
    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS_2))
        .thenReturn(TEST_ANAGRAMS_GROUPS_2);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS_1))
        .thenReturn(FORMATTED_ANAGRAMS_1);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS_2))
        .thenReturn(FORMATTED_ANAGRAMS_2);
    Mockito.when(readerMock.read(TEST_FILE)).thenReturn(FILE_LINES.stream());

    LinearProcessor processor = new LinearProcessor(anagramServiceMock, readerMock, writerMock);
    processor.process(TEST_FILE);

    Mockito.verify(writerMock, Mockito.times(1)).write(FORMATTED_ANAGRAMS_1);
    Mockito.verify(writerMock, Mockito.times(1)).write(FORMATTED_ANAGRAMS_2);
  }

  @Test
  public void testParallelProcessor() throws IOException, InterruptedException {
    Writer writerMock = Mockito.mock(Writer.class);
    Reader readerMock = Mockito.mock(Reader.class);
    AnagramService anagramServiceMock = Mockito.mock(AnagramService.class);

    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS_1))
        .thenReturn(TEST_ANAGRAMS_GROUPS_1);
    Mockito.when(anagramServiceMock.checkAnagrams(TEST_ANAGRAMS_2))
        .thenReturn(TEST_ANAGRAMS_GROUPS_2);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS_1))
        .thenReturn(FORMATTED_ANAGRAMS_1);
    Mockito.when(anagramServiceMock.formatGroup(TEST_ANAGRAMS_GROUPS_2))
        .thenReturn(FORMATTED_ANAGRAMS_2);
    Mockito.when(readerMock.read(TEST_FILE)).thenReturn(FILE_LINES.stream());

    ParallelProcessor processor = new ParallelProcessor(anagramServiceMock, readerMock, writerMock);
    processor.process(TEST_FILE);
    processor.shutDown();

    Mockito.verify(writerMock, Mockito.times(1)).write(FORMATTED_ANAGRAMS_1);
    Mockito.verify(writerMock, Mockito.times(1)).write(FORMATTED_ANAGRAMS_2);
  }
}
