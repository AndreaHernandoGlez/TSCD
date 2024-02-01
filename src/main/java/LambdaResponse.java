import java.util.List;

public class LambdaResponse {
    private List<String> tokens;

    public LambdaResponse(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}

