import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PythonTokenizer implements Tokenizer {

    @Override
    public List<String> tokenize(String sourceCode) {
        List<String> tokens = new ArrayList<>();

        // Expresiones regulares para identificar tokens en Python
        String regex = "\\s+|(;|:|\\{|\\}|\\(|\\)|,|')";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceCode);

        int start = 0;

        while (matcher.find()) {
            String token = sourceCode.substring(start, matcher.start()).trim();
            if (!token.isEmpty()) {
                // Si el token es una cadena entre comillas simples, agregarlo como un solo token
                if (token.startsWith("'") && token.endsWith("'")) {
                    tokens.add(token);
                } else {
                    // Dividir el token en subtokens si contiene comillas simples
                    tokens.addAll(splitTokenWithSingleQuotes(token));
                }
            }

            // Agregar el token encontrado
            tokens.add(matcher.group().trim());

            // Actualizar la posición de inicio
            start = matcher.end();
        }

        // Manejo del último token si la cadena no termina con un separador
        if (start < sourceCode.length()) {
            String lastToken = sourceCode.substring(start).trim();
            if (!lastToken.isEmpty()) {
                // Si el último token es una cadena entre comillas simples, agregarlo como un solo token
                if (lastToken.startsWith("'") && lastToken.endsWith("'")) {
                    tokens.add(lastToken);
                } else {
                    // Dividir el último token en subtokens si contiene comillas simples
                    tokens.addAll(splitTokenWithSingleQuotes(lastToken));
                }
            }
        }

        return tokens;
    }

    private List<String> splitTokenWithSingleQuotes(String token) {
        List<String> subtokens = new ArrayList<>();
        String[] parts = token.split("(')|(')");
        for (String part : parts) {
            if (!part.isEmpty()) {
                subtokens.add(part);
            }
        }
        return subtokens;
    }

}

