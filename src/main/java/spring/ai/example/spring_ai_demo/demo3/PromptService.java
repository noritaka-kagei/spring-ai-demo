package spring.ai.example.spring_ai_demo.demo3;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/chat.st")
    private Resource promptResource;

    public PromptService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(ChatRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);

        Map<String, Object> params = Map.of(
            "role", request.role(),
            "expertise", request.expertise(),
            "style", request.style(),
            "question", request.question()
        );

        Prompt prompt = promptTemplate.create(params);

        return chatClient.prompt(prompt).call().content();
    }
}