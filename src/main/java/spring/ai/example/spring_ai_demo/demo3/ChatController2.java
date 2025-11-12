package spring.ai.example.spring_ai_demo.demo3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class ChatController2 {
    
    private final PromptService promptService;

    public ChatController2(PromptService promptService) {
        this.promptService = promptService;
    }

    @PostMapping("/chat2")
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {

        String response = promptService.chat(request);
        return ResponseEntity.ok(response);
    }    
}
