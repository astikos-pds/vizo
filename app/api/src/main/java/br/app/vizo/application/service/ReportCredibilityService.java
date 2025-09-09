package br.app.vizo.application.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ReportCredibilityService {

    private static final double REPUTATION_WEIGHT = 3;
    private static final double DETAILING_SCORE = 2;
    private static final double EVIDENCE_WEIGHT = 5;

    private static final int MAX_WORDS = 255;
    private static final int MAX_IMAGES = 5;

    public Double calculate(
            double userCredibility,
            String description,
            int numberOfImages,
            boolean userAlreadyReportedProblem
    ) {
        double reputationScore = userCredibility * REPUTATION_WEIGHT;

        long uniqueWords = Arrays.stream(description.split(" ")).distinct().count();
        double detailingScore = (Math.min(uniqueWords, MAX_WORDS) / (double) MAX_WORDS) * DETAILING_SCORE;

        double evidenceScore = (Math.min(numberOfImages, MAX_IMAGES) / (double) MAX_IMAGES) * EVIDENCE_WEIGHT;

        double maxPossibleScore = REPUTATION_WEIGHT + DETAILING_SCORE + EVIDENCE_WEIGHT;
        double rawScore = reputationScore + detailingScore + evidenceScore;

        if (userAlreadyReportedProblem) {
            rawScore -= 20.0;
        }

        return (rawScore / maxPossibleScore) * 100;
    }
}
