package br.app.vizo.application.usecase.user;

import br.app.vizo.application.UseCase;
import br.app.vizo.application.usecase.user.filter.ExistsUserParamsDTO;
import br.app.vizo.core.user.UserRepository;

@UseCase
public class ExistsUserByParamsUseCase {

    private final UserRepository userRepository;

    public ExistsUserByParamsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean execute(ExistsUserParamsDTO params) {
        if (params.document() == null) {
            return this.userRepository.existsByEmail(params.getEmail());
        }

        return this.userRepository.existsByDocumentAndEmail(params.getDocument(), params.getEmail());
    }
}
