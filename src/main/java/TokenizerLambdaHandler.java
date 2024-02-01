import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.GetObjectRequest;
import database.DynamoDBClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class TokenizerLambdaHandler implements RequestHandler<S3Event, LambdaResponse> {
    private final DynamoDBClient dynamoDBClient = new DynamoDBClient();
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();

    @Override
    public LambdaResponse handleRequest(S3Event s3Event, Context context) {
        String bucketName = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String objectKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        String sourceCode = downloadSourceCodeFromS3(bucketName, objectKey);

        File file = new File(objectKey);
        TokenizerFactory tokenizerFactory = new TokenizerFactory();
        Tokenizer tokenizer = tokenizerFactory.getTokenizer(file);
        List<String> tokens = tokenizer.tokenize(sourceCode);

        dynamoDBClient.saveTokensToDynamoDB(tokens);

        return new LambdaResponse(tokens);
    }

    private String downloadSourceCodeFromS3(String bucketName, String objectKey) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
            StringBuilder sourceCode = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCode.append(line).append("\n");
            }
            return sourceCode.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el código fuente desde S3: " + e.getMessage(), e);
        }
    }
}
