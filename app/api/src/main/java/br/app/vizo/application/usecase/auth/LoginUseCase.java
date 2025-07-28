package br.app.vizo.application.usecase.auth;

import br.app.vizo.application.dto.TokenPairDTO;
import br.app.vizo.application.mapper.RefreshTokenMapper;
import br.app.vizo.application.mapper.UserMapper;
import br.app.vizo.application.service.HashService;
import br.app.vizo.application.UseCase;
import br.app.vizo.application.usecase.auth.request.LoginRequestDTO;
import br.app.vizo.config.security.JwtService;
import br.app.vizo.config.security.UserDetailsImpl;
import br.app.vizo.core.user.User;
import br.app.vizo.core.user.UserId;
import br.app.vizo.core.user.password.PasswordHasher;
import br.app.vizo.core.user.token.RefreshToken;
import br.app.vizo.core.user.token.RefreshTokenFactory;
import br.app.vizo.exception.UnauthorizedException;
import br.app.vizo.infrastructure.persistence.RefreshTokenRepository;
import br.app.vizo.infrastructure.persistence.UserRepository;
import br.app.vizo.infrastructure.persistence.entity.RefreshTokenEntity;
import br.app.vizo.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenFactory refreshTokenFactory;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final HashService hashService;

    public TokenPairDTO execute(LoginRequestDTO body) {
        UserEntity userEntity = this.userRepository.findByDocument(body.document()).orElseThrow(
                () -> new UnauthorizedException("Invalid credentials.")
        );

        User user = this.userMapper.toModel(userEntity);

        boolean passwordsMatch = user.passwordMatchesWith(body.password(), passwordHasher);
        if (!passwordsMatch) throw new UnauthorizedException("Invalid credentials.");

        var authConfig = new UsernamePasswordAuthenticationToken(body.document(), body.password());
        Authentication authentication = this.authenticationManager.authenticate(authConfig);
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = this.jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = this.jwtService.generateRefreshToken(userDetails.getUsername());

        RefreshToken created = this.refreshTokenFactory.create(new UserId(user.getId()), this.hashService.hashToken(refreshToken));
        RefreshTokenEntity refreshTokenEntity = this.refreshTokenMapper.toEntity(created);
        this.refreshTokenRepository.save(refreshTokenEntity);

        return new TokenPairDTO(accessToken, refreshToken);
    }
}
