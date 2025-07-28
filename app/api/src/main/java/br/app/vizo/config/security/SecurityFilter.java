package br.app.vizo.config.security;

import br.app.vizo.domain.user.User;
import br.app.vizo.repository.OldUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public final String[] PUBLIC_ROUTES = {
            "/auth/citizen/register",
            "/auth/official/register",
            "/auth/login",
            "/auth/refresh",
            "/auth/verification-requests",
            "/municipalities"
    };

    public final String[] PUBLIC_PATTERNS = {
            "auth"
    };

    private final OldUserRepository oldUserRepository;

    private final JwtService jwtService;

    public SecurityFilter(OldUserRepository oldUserRepository, JwtService jwtService) {
        this.oldUserRepository = oldUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (this.shouldFilter(request)) {
            String token = this.retrieveToken(request);

            if (token != null && this.jwtService.isAccessTokenValid(token)) {
                String subject = this.jwtService.getSubjectFromToken(token);
                Optional<User> user = this.oldUserRepository.findByDocument(subject);

                if (user.isEmpty()) {
                    this.sendError(response, 404, "User not found.");
                    return;
                }

                UserDetails userDetails = new UserDetailsImpl(user.get());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                this.sendError(response, 401, "Missing token.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{ \"message\": \"%s\" }".formatted(message));
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null) {
            String token = header.replace("Bearer", "").strip();
            return token.isBlank() ? null : token;
        }

        return null;
    }

    private boolean shouldFilter(HttpServletRequest request) {
        final String path = request.getRequestURI().replace("/api", "");

        return (!Arrays.stream(this.PUBLIC_ROUTES).toList().contains(path))
                &&
                (Arrays.stream(this.PUBLIC_PATTERNS).noneMatch(path::contains));
    }

    public String[] getPublicRoutes() {
        return this.PUBLIC_ROUTES;
    }
}
