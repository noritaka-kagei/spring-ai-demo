package spring.ai.example.spring_ai_demo.demo4;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {
    
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public RagResponse query(RagRequest request) {

        RetrievalAugmentationAdvisor advisor = createAdvisor(request);

        String answer = chatClient
            .prompt(request.question())
            .advisors(advisor)
            .call()
            .content();

        List<String> sources = extractSources(request);

        return new RagResponse(answer, sources, sources.size());
    }

    private RetrievalAugmentationAdvisor createAdvisor(RagRequest request) {
        var builder = VectorStoreDocumentRetriever.builder()
            .vectorStore(vectorStore)
            .topK(request.topK() != null ? request.topK() : 1)
            .similarityThreshold(0.1);

        return RetrievalAugmentationAdvisor.builder()
            .documentRetriever(builder.build())
            .build();
    }

    private List<String> extractSources(RagRequest request) {
        var searchRequest = SearchRequest.builder()
            .query(request.question())
            .topK(request.topK() != null ? request.topK() : 1)
            .similarityThreshold(0.1)
            .build();

        List<Document> docs = vectorStore.similaritySearch(searchRequest);

        return docs.stream()
            .map(doc -> (String) doc.getMetadata().get("source"))
            .filter(source -> source != null)
            .distinct()
            .toList();
    }
}
