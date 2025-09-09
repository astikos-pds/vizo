package br.app.vizo.application.usecase.problem;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.dto.ProblemDTO;
import br.app.vizo.application.mapper.ProblemMapper;
import br.app.vizo.core.problem.ProblemRepository;

import java.util.List;

@UseCase
public class GetProblemsUseCase {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    public GetProblemsUseCase(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    public List<ProblemDTO> execute() {
        return this.problemRepository.findAll()
                .stream()
                .map(this.problemMapper::toDto)
                .toList();
    }
}
