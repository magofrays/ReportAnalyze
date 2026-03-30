import by.magofrays.AutoReport;
import by.magofrays.InputParserScorer;
import by.magofrays.RougeMetrics;
import by.magofrays.dto.Magazine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MagazineTest {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Magazine> magazineList;
    InputParserScorer inputParser = new InputParserScorer();
    AutoReport autoReport = new AutoReport(300);
    RougeMetrics rougeMetrics = new RougeMetrics();

    public MagazineTest() throws Exception {
        magazineList = new ArrayList<>();

        InputStream magazine = getClass().getResourceAsStream("/gazeta_test.jsonl");

        if (magazine == null) {
            throw new RuntimeException("File not found: gazeta_test.jsonl");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(magazine, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Magazine magazineObj = objectMapper.readValue(line, Magazine.class);
                    magazineList.add(magazineObj);
                }
            }
        }
    }


    @Test
    public void inputParserTest(){
        for(var magazine : magazineList){
            List<String> sentences = inputParser.splitSentences(magazine.getText());
            for(var sentence: sentences){
                var words = inputParser.tokenizeWords(sentence);
                var lemmas = inputParser.lemmatizeWords(words);
                System.out.println(lemmas);
            }
        }
    }

    @Test
    public void autoReportTest(){
        Magazine magazine = magazineList.getFirst();
        List<String> sentences = autoReport.getReport(magazine.getText());
        String result = inputParser.buildText(sentences);
        System.out.println(magazine.getSummary());
        System.out.println("===================");
        System.out.println(result);
        RougeMetrics.RougeResult rougeResult = rougeMetrics.computeRouge(result, magazine.getSummary());
        System.out.println(rougeResult.rouge1() + " " + rougeResult.rouge2() + " " + rougeResult.rougeL());
    }

    @Test
    public void autoReportAllTest(){
        for(var magazine: magazineList){

        }
    }

}
