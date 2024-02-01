import java.io.File;

public class TokenizerFactory {
    public Tokenizer getTokenizer(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".java")) {
            return new JavaTokenizer();
        } else if (fileName.endsWith(".py")) {
            return new PythonTokenizer();
        } else {
            return new NullTokenizer();
        }
    }
}
