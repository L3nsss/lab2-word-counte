package ua.kpi.comsys.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

class WordCounterTest {
    private final WordCounter counter = new WordCounter();

    @Test
    void countWordsRejectsNullList() {
        assertThrows(IllegalArgumentException.class, () -> counter.countWords(null));
    }

    @Test
    void getUniqueWordsRejectsNullList() {
        assertThrows(IllegalArgumentException.class, () -> counter.getUniqueWords(null));
    }

    @Test
    void emptyInputReturnsEmptyCollections() {
        assertTrue(counter.countWords(List.of()).isEmpty());
        assertTrue(counter.getUniqueWords(List.of()).isEmpty());
    }

    @Test
    void countsRepeatedWordsAcrossLinesWithoutRegardToCase() {
        List<String> lines = List.of("Java java GRADLE", "gradle Java collections");

        assertEquals(Map.of("java", 3, "gradle", 2, "collections", 1),
                counter.countWords(lines));
        assertEquals(Set.of("java", "gradle", "collections"),
                counter.getUniqueWords(lines));
    }

    @Test
    void punctuationWhitespaceAndUnderscoresSeparateWords() {
        assertEquals(Map.of("one", 1, "two", 1, "three", 1, "four", 1, "five", 1),
                counter.countWords(List.of("one,two.three_four\tfive!")));
    }

    @Test
    void digitsBelongToWordsAndCanAlsoFormWords() {
        assertEquals(Map.of("java17", 2, "123", 2, "a1b2", 1),
                counter.countWords(List.of("Java17 java17 123 123 A1B2")));
    }

    @Test
    void emptyStringsAndDelimiterOnlyLinesProduceNoWords() {
        List<String> lines = List.of("", " \t\n", "...,,!?_-+");

        assertTrue(counter.countWords(lines).isEmpty());
        assertTrue(counter.getUniqueWords(lines).isEmpty());
    }

    @Test
    void nonLatinCharactersAreSeparatorsRatherThanPartsOfWords() {
        assertEquals(Map.of("java", 1, "na", 1, "ve", 1, "abc", 1, "123", 1),
                counter.countWords(List.of("ПривітJavaсвіт naïve abcЖ123")));
        assertTrue(counter.getUniqueWords(List.of("Україна Київ")).isEmpty());
    }

    @Test
    void ignoresNullElementsWithoutDiscardingOtherLines() {
        List<String> lines = Arrays.asList(null, "Java", null, "JAVA");

        assertEquals(Map.of("java", 2), counter.countWords(lines));
        assertEquals(Set.of("java"), counter.getUniqueWords(lines));
    }

    @Test
    @ResourceLock("default-locale")
    void lowercasingIsIndependentOfDefaultLocale() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));

            assertEquals(Map.of("i", 2, "title", 1),
                    counter.countWords(List.of("I i TITLE")));
            assertEquals(Set.of("i"), counter.getUniqueWords(List.of("I")));
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    @Test
    void returnedCollectionsAndInputAreIndependent() {
        List<String> lines = new ArrayList<>(List.of("Java Gradle Java"));
        Map<String, Integer> counts = counter.countWords(lines);
        Set<String> words = counter.getUniqueWords(lines);

        counts.clear();
        words.add("new");

        assertEquals(List.of("Java Gradle Java"), lines);
        assertEquals(Map.of("java", 2, "gradle", 1), counter.countWords(lines));
        assertEquals(Set.of("java", "gradle"), counter.getUniqueWords(lines));
    }
}
