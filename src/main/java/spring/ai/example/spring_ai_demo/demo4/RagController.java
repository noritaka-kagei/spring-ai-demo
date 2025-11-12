package spring.ai.example.spring_ai_demo.demo4;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class RagController {
    
    private final PdfService pdfService;
    private final RagService ragService;

    public RagController(PdfService pdfService, RagService ragService) {
        this.pdfService = pdfService;
        this.ragService = ragService;
    }

    @GetMapping("/load")
    public ResponseEntity<String> load() {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:/documents/WebOTX Application Server Manual.pdf");

        List<Document> docs = pdfService.loadPdf(resource);
        pdfService.addDocuments(docs);
        pdfService.saveVectorStore();
        pdfService.loadVectorStore();

        return ResponseEntity.ok("document is saved to vector store");
    }
    

    @PostMapping("/rag")
    public ResponseEntity<RagResponse> query(@RequestBody RagRequest request) {
        RagResponse response = ragService.query(request);
        return ResponseEntity.ok(response);
    }
}
