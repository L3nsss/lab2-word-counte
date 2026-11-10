package ua.kpi.comsys.collections;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Counts tokens made up of Latin letters and digits. */
public class WordCounter {
    private static final Pattern WORD_PATTERN = Pattern.compile("[A-Za-z0-9]+");

    /**
     * Counts words without regard to letter case. Null elements are ignored.
     * The result follows the order in which words first occur.
     *
     * @param textLines lines to process
     * @return an independent map of word frequencies
     * @throws IllegalArgumentException if textLines is null
     */
    public Map<String, Integer> countWords(List<String> textLines) {
        if (textLines == null) {
            throw new IllegalArgumentException("textLines cannot be null");
        }

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String line : textLines) {
            if (line == null) {
                continue;
            }
            Matcher matcher = WORD_PATTERN.matcher(line);
            while (matcher.find()) {
                String word = matcher.group().toLowerCase(Locale.ROOT);
                counts.merge(word, 1, Integer::sum);
            }
        }
        return counts;
    }

    /**
     * Returns distinct lowercase words in their order of first occurrence.
     * Null elements are ignored.
     *
     * @param textLines lines to process
     * @return an independent set of words
     * @throws IllegalArgumentException if textLines is null
     */
    public Set<String> getUniqueWords(List<String> textLines) {
        return new LinkedHashSet<>(countWords(textLines).keySet());
    }
}
