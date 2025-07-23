package br.app.vizo.domain.report;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

}
