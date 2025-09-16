package br.app.vizo.application.service;

import br.app.vizo.core.problem.ProblemType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Service
public class ReportAnalysisService {

    public record AnalysisRequest(
          String description,
          Set<String> images_urls,
          ProblemType[] categories
    ) {}

    public record AnalysisResponse(
            ProblemType predicted_type,
            double confidence,
            double text_image_consistency,
            double[] per_image_scores
    ) {}

    private final WebClient webClient;

    public ReportAnalysisService(WebClient.Builder builder, @Value("${analyser.url}") String analyserUrl) {
        this.webClient = builder.baseUrl(analyserUrl).build();
    }

    public AnalysisResponse analyseReport(String description, Set<String> imagesUrls) {
        AnalysisRequest body = new AnalysisRequest(description, imagesUrls, ProblemType.values());

         return webClient.post().uri("/classify")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AnalysisResponse.class)
                .block();
    }

}
