package by.magofrays;

import java.io.InputStream;
import java.util.*;

public class InputParserScorer {

    Double ADD_SCORE = 0.7;
    Double ADD_SCORE_MULTIPLY = 0.3;
    Set<String> STOP_WORDS = Set.of(
            "и", "в", "во", "на", "с", "со", "как", "а", "но", "или", "для", "по",
            "же", "из", "за", "под", "над", "к", "у", "о", "об", "бы", "что", "это",
            "этот", "эта", "эти", "тот", "та", "те", "все", "всё", "весь", "который",
            "которое", "которая", "которые", "так", "также", "ещё", "уже", "даже",
            "только", "если", "чтобы", "будет", "было", "были", "быть", "этих", "тех",
            "них", "вам", "нас", "вас", "им", "его", "её", "их", "мой", "твой", "свой",
            "наш", "ваш", "один", "одна", "одно", "одни", "два", "две", "три", "четыре",
            "пять", "больше", "меньше", "очень", "тоже", "кто", "где", "когда", "почему",
            "зачем", "такой", "такая", "такое", "такие"
    );
    public String readText(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder text = new StringBuilder();
        while(scanner.hasNextLine()){
            text.append(scanner.nextLine());
        }
        return text.toString();
    }

    public List<String> splitSentences(String text){
        List<String> sentences = new ArrayList<>();
        String[] result = text.strip().split("(?<=[.!?])\\s+");
        for(var elem: result){
            sentences.add(
                    elem.strip()
            );
        }
        return sentences;
    }

    public List<String> tokenizeWords(String sentence){
        sentence = sentence.replaceAll("[.!?:;]", "").toLowerCase();
        List<String> tokens = new ArrayList<>();
        for(var word: sentence.split(" ")){
            if(!STOP_WORDS.contains(word)){
                tokens.add(word);
            }
        }
        return tokens;
    }

    public List<String> lemmatizeWords(List<String> words){
        List<String> lemmas = new ArrayList<>();
        for(var word: words){
            lemmas.add(
                    MorphAnalyzer.getInstance().lemma(word)
            );
        }
        return lemmas;
    }

    Map<String, Integer> countWordFrequencies(List<List<String>> sentences){
        Map<String, Integer> freq = new HashMap<>();
        for(var sentence: sentences){
            for(var lemma : sentence){
                freq.put(lemma, freq.getOrDefault(lemma, 0) + 1);
            }
        }
        return freq;
    }

    public TreeMap<Double, Integer> scoreSentences(List<List<String>> sentencesLem, Map<String, Integer> wordFreq) {
        TreeMap<Double, Integer> scores = new TreeMap<>();
        Double addScore = ADD_SCORE;
        int i = 0;
        for (List<String> sentLem : sentencesLem) {
            double score = 0;
            for (String w : sentLem) {
                score += wordFreq.getOrDefault(w, 0);
            }
            scores.put(score * (1 + addScore), i);
            addScore *= ADD_SCORE_MULTIPLY;
            i++;
        }
        return scores;
    }

    public String buildText(List<String> sentences){
        StringBuilder stringBuilder = new StringBuilder(" ");
        sentences.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

}