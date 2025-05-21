package br.app.vizo.config.security;

import br.app.vizo.domain.user.User;
import br.app.vizo.repository.UserRepository;
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

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public final String[] PUBLIC_ROUTES = {
            "/auth/register/citizen",
            "/auth/register/municipality",
            "/auth/login",
            "/auth/refresh"
    };

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public SecurityFilter(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
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
                User user = this.userRepository.findByDocument(subject).orElseThrow(
                        () -> new RuntimeException("User not found.")
                );
                UserDetails userDetails = new UserDetailsImpl(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("Missing token.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) return header.replace("Bearer ", "");

        return null;
    }

    private boolean shouldFilter(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/api", "");
        return !Arrays.stream(this.PUBLIC_ROUTES).toList().contains(path);
    }

    public String[] getPublicRoutes() {
        return this.PUBLIC_ROUTES;
    }
}
