package spring.ai.example.spring_ai_demo.demo3;

public record ChatRequest(
    String role,
    String expertise,
    String style,
    String question
) {}