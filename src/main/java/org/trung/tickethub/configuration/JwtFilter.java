package org.trung.tickethub.configuration;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.trung.tickethub.service.JwtService;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.trung.tickethub.configuration.SecurityConfig.WHITE_LIST_PATHS;
import static org.trung.tickethub.constant.TokenType.ACCESS_TOKEN;
@Setter
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String servletPath = request.getServletPath();

        // Check if the servlet path matches any whitelisted path
        for (String path : WHITE_LIST_PATHS) {
            if (pathMatcher.match(path, servletPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        final String authorization = request.getHeader(AUTHORIZATION);

        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String token = authorization.substring("Bearer ".length());

            final String phoneNumber = jwtService.extractUsername(token, ACCESS_TOKEN);

            if (StringUtils.isNotEmpty(phoneNumber) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtService.isValid(token, ACCESS_TOKEN, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}