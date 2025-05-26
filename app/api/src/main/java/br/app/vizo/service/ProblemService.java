package br.app.vizo.service;

import br.app.vizo.controller.response.ProblemDTO;
import br.app.vizo.mapper.ProblemMapper;
import br.app.vizo.repository.ProblemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    public ProblemService(ProblemRepository problemRepository, ProblemMapper problemMapper) {
        this.problemRepository = problemRepository;
        this.problemMapper = problemMapper;
    }

    public List<ProblemDTO> getProblems() {
        return this.problemRepository.findAll()
                .stream()
                .map(this.problemMapper::toDto)
                .collect(Collectors.toList());
    }
}
