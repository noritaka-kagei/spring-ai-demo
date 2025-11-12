package spring.ai.example.spring_ai_demo.demo4;

import java.io.File;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PdfService {
    
    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;

    @Value("${app.vector-store.file-path}")
    private String vectorStoreFilePath;

    public PdfService(VectorStore vectorStore, TokenTextSplitter textSplitter) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
    }

    public List<Document> loadPdf(Resource resource) {
        String filename = resource.getFilename();

        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
        List<Document> docs = pdfReader.get();

        List<Document> splitDocs = textSplitter.apply(docs);
        List<Document> validDocs = splitDocs.stream()
            .filter(doc -> {
                String content = doc.getFormattedContent().replaceAll("[ \t]+\n", "\n");
                return content != null && !content.trim().isEmpty(); 
            })
            .toList();
        
        validDocs.forEach(doc -> {
            doc.getMetadata().put("source", filename);
            doc.getMetadata().put("type", "pdf");
        });

        return validDocs;
    }

    public void addDocuments(List<Document> docs) {
        if (docs != null && !docs.isEmpty()) {
            vectorStore.add(docs);
        }
    }

    public void saveVectorStore() {
        if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
            simpleVectorStore.save(new File(vectorStoreFilePath));
        }
    }

    public void loadVectorStore() {
        if (vectorStore instanceof SimpleVectorStore simpleVectorStore) {
            simpleVectorStore.load(new File(vectorStoreFilePath));
        }
    }
}
