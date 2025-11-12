package spring.ai.example.spring_ai_demo.demo4;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Value("${app.vector-store.file-path}")
    private String vectorStoreFilePath;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        File vectorStoreFile = Paths.get(vectorStoreFilePath).toFile();
        if (vectorStoreFile.exists()) {
            vectorStore.load(vectorStoreFile);
        }

        return vectorStore;
    }
}
