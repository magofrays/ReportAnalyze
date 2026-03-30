package by.magofrays;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

@NoArgsConstructor
public class MorphAnalyzer {
    private final static MyStem mystemAnalyzer =
            new Factory("-igd --eng-gr --format json --weight")
                    .newMyStem("3.0", Option.apply(null)).get();

    @Getter
    private static final MorphAnalyzer instance = new MorphAnalyzer();



    public String lemma(String word) {
        try {
            final Iterable<Info> result =
                    JavaConversions.asJavaIterable(
                            mystemAnalyzer
                                    .analyze(Request.apply(word))
                                    .info()
                                    .toIterable());

            for (Info info : result) {
                if (info.lex().isDefined()) {
                    return info.lex().get();
                }
            }
            return word;
        } catch (MyStemApplicationException e) {
            throw new RuntimeException(e);
        }
    }

}