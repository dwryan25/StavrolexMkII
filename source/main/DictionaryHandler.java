package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class DictionaryHandler {

    private final GridData gridData;

    private static final Path dictPath = Path.of("words.txt");

    private ArrayList<String> possAcrossWords;
    private ArrayList<String> possDownWords;


    public DictionaryHandler(GridData gridData){
        this.gridData = gridData;

        possAcrossWords = new ArrayList<>();
        possDownWords = new ArrayList<>();
    }


    public void updateWordLists() throws IOException {

        int selectedRow = gridData.getSelectedRow();
        int selectedCol = gridData.getSelectedCol();

        String acrossRegex = toRegex(gridData.getAcrossWord(selectedRow, selectedCol));
        String downRegex = toRegex(gridData.getDownWord(selectedRow, selectedCol));

        possAcrossWords = findWords( acrossRegex);
        possDownWords = findWords(downRegex);
    }

    private ArrayList<String> findWords(String p) throws IOException {
        Pattern pattern = Pattern.compile(p, Pattern.CASE_INSENSITIVE);
        try (Stream<String> lines = Files.lines(dictPath)){
            return (ArrayList<String>) lines
                    .map(String::trim)
                    .filter(word -> !word.isEmpty())
                    .filter(word -> pattern.matcher(word).matches())
                    .toList();
        }
    }

    private String toRegex(String word){
        StringBuilder regexPattern = new StringBuilder("^");
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == '-'){
                regexPattern.append('.');
            }
            else{
                regexPattern.append(word.charAt(i));
            }
        }
        regexPattern.append('$');
        return regexPattern.toString();
    }
}
