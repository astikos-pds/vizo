package br.app.vizo.service;

import br.app.vizo.controller.filter.ReportFilter;
import br.app.vizo.controller.request.CreateReportRequestDTO;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.problem.ProblemStatus;
import br.app.vizo.domain.report.Report;
import br.app.vizo.dto.ReportDTO;
import br.app.vizo.domain.report.ReportImage;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.CitizenRepository;
import br.app.vizo.repository.ProblemRepository;
import br.app.vizo.repository.ReportImageRepository;
import br.app.vizo.repository.ReportRepository;
import br.app.vizo.util.DateUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportImageRepository reportImageRepository;
    private final ProblemRepository problemRepository;
    private final CitizenRepository citizenRepository;
    private final ReportMapper reportMapper;
    private final GeometryFactory geometryFactory;

    public ReportService(
            ReportRepository reportRepository,
            ReportImageRepository reportImageRepository,
            ProblemRepository problemRepository,
            CitizenRepository citizenRepository,
            ReportMapper reportMapper,
            GeometryFactory geometryFactory
    ) {
        this.reportRepository = reportRepository;
        this.reportImageRepository = reportImageRepository;
        this.problemRepository = problemRepository;
        this.citizenRepository = citizenRepository;
        this.reportMapper = reportMapper;
        this.geometryFactory = geometryFactory;
    }

    @Transactional
    public ReportDTO createReport(CreateReportRequestDTO body, Authentication authentication) {
        Citizen citizen = this.citizenRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        Point coordinates = this.geometryFactory.createPoint(new Coordinate(body.latitude(), body.longitude()));

        Optional<Problem> existingProblem = this.findRelatedProblem(body.latitude(), body.longitude(), 5.0);
        boolean isProblemAlreadyReported = false;

        if (existingProblem.isPresent()) {
            isProblemAlreadyReported =
                    this.isProblemAlreadyReportedByCitizen(existingProblem.get().getId(), citizen.getId());
        }

        Problem problem = existingProblem.orElseGet(
                () -> new Problem(ProblemStatus.ANALYSIS, coordinates, body.problemType(), 0.0)
        );

        Double accumulatedCredibility = problem.getAccumulatedCredibility() + this.calculateReportCredibility(
                citizen.getCredibilityPoints(),
                body.description(),
                body.imagesUrls().size(),
                isProblemAlreadyReported
        );

        problem.setAccumulatedCredibility(accumulatedCredibility);
        problem.setLastReportedAt(DateUtil.now());

        if (problem.getAccumulatedCredibility() >= 100) {
            problem.setValidated(true);
        }

        problem = this.problemRepository.save(problem);

        Report report = new Report();
        report.setDescription(body.description());
        report.setCoordinates(coordinates);
        report.setCitizen(citizen);
        report.setProblem(problem);
        report = this.reportRepository.save(report);

        final Report finalReport = report;
        List<ReportImage> images = body.imagesUrls().stream().map((imageUrl) -> {
            ReportImage image = new ReportImage();
            image.setUrl(imageUrl);
            image.setReport(finalReport);

            return image;
        }).collect(Collectors.toList());

        report.setImages(this.reportImageRepository.saveAll(images));

        return this.reportMapper.toDto(report);
    }

    public Page<ReportDTO> getReports(
            ReportFilter filter,
            Pageable pageable,
            Authentication authentication
    ) {
        Citizen citizen = this.citizenRepository.findByDocument(authentication.getName()).orElseThrow(
                () -> new NotFoundException("User not found.")
        );

        if (filter.latitude() == null || filter.longitude() == null) {
            return this.reportRepository
                    .findAllByCitizenId(citizen.getId(), pageable)
                    .map(this.reportMapper::toDto);
        }

        return this.reportRepository
                .findAllByCitizenIdWithinDistance(
                        citizen.getId(),
                        filter.latitude(),
                        filter.longitude(),
                        Objects.requireNonNullElse(filter.radius(), 1.0),
                        pageable
                ).map(this.reportMapper::toDto);
    }

    private Optional<Problem> findRelatedProblem(Double latitude, Double longitude, Double radius) {
        return this.problemRepository.findNearestWithinDistance(latitude, longitude, radius);
    }

    private Boolean isProblemAlreadyReportedByCitizen(UUID problemId, UUID citizenId) {
        return this.reportRepository.existsByProblemIdAndCitizenId(problemId, citizenId);
    }

    private static final double REPUTATION_WEIGHT = 3;
    private static final double DETAILING_SCORE = 2;
    private static final double EVIDENCE_WEIGHT = 5;

    private static final int MAX_WORDS = 255;
    private static final int MAX_IMAGES = 5;
    private Double calculateReportCredibility(
            Double citizenCredibility,
            String description,
            int numberOfImages,
            boolean isProblemAlreadyReportedByCitizen
    ) {
        double reputationScore = citizenCredibility * REPUTATION_WEIGHT;

        long uniqueWords = Arrays.stream(description.split(" ")).distinct().count();
        double detailingScore = (Math.min(uniqueWords, MAX_WORDS) / (double) MAX_WORDS) * DETAILING_SCORE;

        double evidenceScore = (Math.min(numberOfImages, MAX_IMAGES) / (double) MAX_IMAGES) * EVIDENCE_WEIGHT;

        double maxPossibleScore = REPUTATION_WEIGHT + DETAILING_SCORE + EVIDENCE_WEIGHT;
        double rawScore = reputationScore + detailingScore + evidenceScore;

        if (isProblemAlreadyReportedByCitizen) {
            rawScore -= 20.0;
        }

        return (rawScore / maxPossibleScore) * 100;
    }
}
