package br.app.vizo.repository;

import br.app.vizo.domain.report.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldReportImageRepository extends JpaRepository<ReportImage, Long> {
}
