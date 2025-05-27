package br.app.vizo.service;

import br.app.vizo.controller.request.CreateReportRequestDTO;
import br.app.vizo.domain.problem.Problem;
import br.app.vizo.domain.problem.ProblemStatus;
import br.app.vizo.domain.report.Report;
import br.app.vizo.controller.response.ReportDTO;
import br.app.vizo.domain.user.Citizen;
import br.app.vizo.exception.http.ConflictException;
import br.app.vizo.exception.http.NotFoundException;
import br.app.vizo.mapper.ReportMapper;
import br.app.vizo.repository.CitizenRepository;
import br.app.vizo.repository.ProblemRepository;
import br.app.vizo.repository.ReportRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProblemRepository problemRepository;
    private final CitizenRepository citizenRepository;
    private final ReportMapper reportMapper;
    private final GeometryFactory geometryFactory;

    public ReportService(
            ReportRepository reportRepository,
            ProblemRepository problemRepository,
            CitizenRepository citizenRepository,
            ReportMapper reportMapper,
            GeometryFactory geometryFactory
    ) {
        this.reportRepository = reportRepository;
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

        Optional<Problem> existingProblem = this.findRelatedProblem(body.latitude(), body.longitude());

        if (existingProblem.isPresent()) {
            if (this.isProblemAlreadyReportedByCitizen(existingProblem.get().getId(), citizen.getId())) {
                throw new ConflictException("Problem already reported by citizen.");
            }
        }

        Problem problem = existingProblem.orElseGet(() -> new Problem(ProblemStatus.ANALYSIS, coordinates, 0.0));

        Double accumulatedCredibility = problem.getAccumulatedCredibility() + (citizen.getCredibilityPoints() * 2);

        problem.setAccumulatedCredibility(accumulatedCredibility);
        problem.setLastReportedAt(Instant.now());

        if (problem.getAccumulatedCredibility() >= 10) {
            problem.setValidated(true);
        }

        problem = this.problemRepository.save(problem);

        Report report = new Report();
        report.setDescription(body.description());
        report.setImageUrl(body.imageUrl());
        report.setCoordinates(coordinates);
        report.setCitizen(citizen);
        report.setProblem(problem);

        report = this.reportRepository.save(report);

        return this.reportMapper.toDto(report);
    }

    private Optional<Problem> findRelatedProblem(Double latitude, Double longitude) {
        return this.problemRepository.findNearestWithinDistance(latitude, longitude, 20.0);
    }

    private Boolean isProblemAlreadyReportedByCitizen(UUID problemId, UUID citizenId) {
        return this.reportRepository.existsByProblemIdAndCitizenId(problemId, citizenId);
    }
}
