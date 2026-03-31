package by.magofrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RougeMetrics {

    InputParserScorer inputParserScorer = new InputParserScorer();

    public double rouge1(List<String> lemmasRef, List<String> lemmasCand) {
        Map<String, Integer> refCounts = new HashMap<>();
        Map<String, Integer> candCounts = new HashMap<>();

        for (String lemma : lemmasRef) {
            refCounts.put(lemma, refCounts.getOrDefault(lemma, 0) + 1);
        }

        for (String lemma : lemmasCand) {
            candCounts.put(lemma, candCounts.getOrDefault(lemma, 0) + 1);
        }

        int intersection = 0;
        for (Map.Entry<String, Integer> entry : refCounts.entrySet()) {
            String word = entry.getKey();
            int refCount = entry.getValue();
            int candCount = candCounts.getOrDefault(word, 0);
            intersection += Math.min(refCount, candCount);
        }

        int candTotal = candCounts.values().stream().mapToInt(Integer::intValue).sum();
        int refTotal = refCounts.values().stream().mapToInt(Integer::intValue).sum();

        double precision = candTotal > 0 ? (double) intersection / candTotal : 0;
        double recall = refTotal > 0 ? (double) intersection / refTotal : 0;

        if (precision + recall == 0) {
            return 0.0;
        }

        return 2 * precision * recall / (precision + recall);
    }

    public double rouge2(List<String> lemmasRef, List<String> lemmasCand) {
        List<String> bigramsRef = new ArrayList<>();
        List<String> bigramsCand = new ArrayList<>();

        for (int i = 0; i < lemmasRef.size() - 1; i++) {
            bigramsRef.add(lemmasRef.get(i) + " " + lemmasRef.get(i + 1));
        }

        for (int i = 0; i < lemmasCand.size() - 1; i++) {
            bigramsCand.add(lemmasCand.get(i) + " " + lemmasCand.get(i + 1));
        }

        Map<String, Integer> refCounts = new HashMap<>();
        Map<String, Integer> candCounts = new HashMap<>();

        for (String bigram : bigramsRef) {
            refCounts.put(bigram, refCounts.getOrDefault(bigram, 0) + 1);
        }

        for (String bigram : bigramsCand) {
            candCounts.put(bigram, candCounts.getOrDefault(bigram, 0) + 1);
        }

        int intersection = 0;
        for (Map.Entry<String, Integer> entry : refCounts.entrySet()) {
            String bigram = entry.getKey();
            int refCount = entry.getValue();
            int candCount = candCounts.getOrDefault(bigram, 0);
            intersection += Math.min(refCount, candCount);
        }

        int candTotal = candCounts.values().stream().mapToInt(Integer::intValue).sum();
        int refTotal = refCounts.values().stream().mapToInt(Integer::intValue).sum();

        double precision = candTotal > 0 ? (double) intersection / candTotal : 0;
        double recall = refTotal > 0 ? (double) intersection / refTotal : 0;

        if (precision + recall == 0) {
            return 0.0;
        }

        return 2 * precision * recall / (precision + recall);
    }

    public double rougeL(List<String> lemmasRef, List<String> lemmasCand) {
        int n = lemmasRef.size();
        int m = lemmasCand.size();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (lemmasRef.get(i).equals(lemmasCand.get(j))) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                }
            }
        }

        int lcs = dp[n][m];

        double precision = m > 0 ? (double) lcs / m : 0;
        double recall = n > 0 ? (double) lcs / n : 0;

        if (precision + recall == 0) {
            return 0.0;
        }

        return 2 * precision * recall / (precision + recall);
    }

    public RougeResult computeRouge(String result, String ref) {
        List<String> lemmasRef = lemmatizeText(ref);
        List<String> lemmasResult = lemmatizeText(result);
        double rouge1 = rouge1(lemmasRef, lemmasResult);
        double rouge2 = rouge2(lemmasRef, lemmasResult);
        double rougeL = rougeL(lemmasRef, lemmasResult);
        return new RougeResult(rouge1, rouge2, rougeL);
    }

    private List<String> lemmatizeText(String text) {
        return inputParserScorer.lemmatizeWords(inputParserScorer.tokenizeWords(text));
    }


    public record RougeResult(Double rouge1, Double rouge2, Double rougeL) {
    }
}