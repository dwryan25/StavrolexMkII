package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class DictionaryHandler {

    public record Entry(String word, int score) {}

    private final Map<Integer, List<Entry>> byLength = new HashMap<>();

    public void add(String rawWord, int score) {
        String word = rawWord.toUpperCase().replaceAll("[^A-Z]", "");
        if (word.isEmpty()) {
            return;
        }
        byLength.computeIfAbsent(word.length(), k -> new ArrayList<>())
                .add(new Entry(word, score));
    }//end add()


    public void load(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                int score = 0;

                if (parts.length > 1) {
                    try {
                        score = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
                add(parts[0], score);
            }
        }

        for(List<Entry> bucket : byLength.values()){
            bucket.sort(Comparator.comparingInt(Entry::score).reversed());
        }
    }//end load()

    public List<Entry> wordsOfLength(int length){
        return byLength.getOrDefault(length, List.of());
    }


    public List<Entry> match(String pattern, CellConstraint[] c){
        String p = pattern.toUpperCase();
        List<Entry>results = new ArrayList<>();
        for (Entry e : wordsOfLength(p.length())){
            if(fits(e.word(), p, c)){
                results.add(e);
            }
        }
        return results;
    }

    private static boolean fits(String word, String pattern, CellConstraint[] cellConstraint){
        for (int i = 0; i < pattern.length(); i++){
            char c = pattern.charAt(i);
            boolean wildcard = (c == '-' || c == '.' || c == '?');
            if(!wildcard && c != word.charAt(i)){
                return false;
            }
            else if (i < cellConstraint.length && !cellConstraint[i].accepts(word.charAt(i))){
                return false;
            }
        }
        return true;
    }

}//end DictionaryHandler