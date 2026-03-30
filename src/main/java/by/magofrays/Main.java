package by.magofrays;


import java.util.List;

public class Main {


    static void main(String[] args) {
        InputParserScorer inputParser = new InputParserScorer();
        String text = inputParser.readText(System.in);
        List<String> sentences = inputParser.splitSentences(text);


        for(var sentence: sentences){
            List<String> sentenceWords = inputParser.tokenizeWords(sentence);
            for (var lemma : inputParser.lemmatizeWords(sentenceWords)){
                System.out.println(lemma);
            }
        }
    }
}