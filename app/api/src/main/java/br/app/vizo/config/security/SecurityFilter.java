package br.app.vizo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public final String[] PUBLIC_ROUTES = {
            "/municipalities"
    };

    public final String[] PUBLIC_PATTERNS = {
            "auth",
            "users"
    };

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public SecurityFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!this.shouldFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.retrieveToken(request);

        if (token == null || !this.jwtService.isAccessTokenValid(token)) {
            this.sendError(response);
            return;
        }

        String subject = this.jwtService.getSubjectFromToken(token);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("{ \"message\": \"%s\" }".formatted("Missing token."));
    }

    private String retrieveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header == null) {
            return null;
        }

        String token = header.replace("Bearer", "").strip();
        return token.isBlank() ? null : token;
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
