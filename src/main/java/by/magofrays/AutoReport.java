package by.magofrays;

import java.util.*;

public class AutoReport {
    InputParserScorer inputParserScorer;
    Integer maxSize;

    public AutoReport(Integer maxSize){
        this.maxSize = maxSize;
        inputParserScorer = new InputParserScorer();
    }

    public List<String> getReport(String text){
        List<String> sentences = inputParserScorer.splitSentences(text);
        List<List<String>> lemmas = new ArrayList<>();
        for(var sentence: sentences){
            lemmas.add(inputParserScorer.tokenizeWords(sentence));
        }
        Map<String, Integer> wordFrequencies = inputParserScorer.countWordFrequencies(lemmas);
        TreeMap<Double, Integer> scores = inputParserScorer.scoreSentences(lemmas, wordFrequencies);
        int symbolSize = 0;
        TreeMap<Integer, String> result = new TreeMap<>();
        for(Integer sentenceIndex :scores.descendingMap().values()){
            String sentence = sentences.get(sentenceIndex);
            symbolSize += sentence.length();
            if(symbolSize > maxSize){
                break;
            }
            result.put(sentenceIndex, sentence);
        }
        return new ArrayList<>(result.values());
    }
}
