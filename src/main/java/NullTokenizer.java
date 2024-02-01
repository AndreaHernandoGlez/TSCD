import java.util.Collections;
import java.util.List;

public class NullTokenizer implements Tokenizer {
    @Override
    public List<String> tokenize(String sourceCode) {
        return Collections.emptyList();
    }
}