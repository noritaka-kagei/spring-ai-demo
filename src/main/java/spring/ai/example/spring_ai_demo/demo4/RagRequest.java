package spring.ai.example.spring_ai_demo.demo4;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RagRequest(
    @JsonProperty("question")
    String question,

    @JsonProperty("filterExpression")
    String filterExpression,

    @JsonProperty("topK")
    Integer topK
) {
    public RagRequest(String question) {
        this(question, null, 1);
    }
}