import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String sourceCode) {
        // Utilizar expresiones regulares para dividir el código
        List<String> tokens = Arrays.asList(sourceCode.split("\\b"));
        Pattern pattern = Pattern.compile("\\b|(?<=[\\p{Punct}\\w])\\p{Punct}(?=[\\p{Punct}\\w])|(?<=\\w)\\p{Punct}|\\p{Punct}(?=\\w)");

        return tokens.stream()
                .map(token -> {
                    Matcher matcher = pattern.matcher(token);
                    return matcher.replaceAll(" ");
                })
                .flatMap(token -> Arrays.stream(token.split("\\s+")))
                .filter(token -> !token.isEmpty())
                .collect(Collectors.toList());
    }
}
