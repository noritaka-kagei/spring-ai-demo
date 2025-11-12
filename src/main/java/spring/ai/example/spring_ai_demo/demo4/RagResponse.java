package spring.ai.example.spring_ai_demo.demo4;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RagResponse(
    @JsonProperty("answer")
    String answer,

    @JsonProperty("sources")
    List<String> sources,

    @JsonProperty("documentsUsed")
    int documentsUsed
) {
}
