package by.magofrays;

import java.util.*;

public class AutoReport {
    InputParserScorer inputParserScorer;
    Integer maxSize;

    public AutoReport(Integer maxSize){
        this.maxSize = maxSize;
        inputParserScorer = new InputParserScorer();
    }

    public String getReport(String text){
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
            result.put(sentenceIndex, sentence);
            if(symbolSize > maxSize){
                break;
            }
        }
        sentences = new ArrayList<>(result.values());
        return inputParserScorer.buildText(sentences).substring(0, maxSize);
    }
}
