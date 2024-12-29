package ma.ensaj.StaySafe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();

        System.out.println("Request path: " + requestPath);
        System.out.println("Auth header present: " + (authHeader != null));

        if (requestPath.equals("/api/auth/login") ||
                requestPath.equals("/api/auth/signup") ||
                requestPath.equals("/api/auth/forgot-password")) {
            System.out.println("Skipping authentication for public endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            System.out.println("Extracted username from token: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("Loaded user details: " + userDetails.getUsername());

                if (jwtService.validateToken(token, userDetails)) {
                    System.out.println("Token is valid");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set in SecurityContext");
                } else {
                    System.out.println("Token validation failed");
                }
            }
        } else {
            System.out.println("No Bearer token found in request");
        }

        filterChain.doFilter(request, response);
    }
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String requestPath = request.getRequestURI();
//
//        // Ignore JWT validation for non-secured endpoints
////        if (requestPath.startsWith("/api/auth/")) {
////            filterChain.doFilter(request, response);
////            return;
////        }
//        // Ne ignorer que les endpoints publics spécifiques
//        if (requestPath.equals("/api/auth/login") ||
//                requestPath.equals("/api/auth/signup") ||
//                requestPath.equals("/api/auth/forgot-password")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            String username = jwtService.extractUsername(token);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                if (jwtService.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails, null, userDetails.getAuthorities());
//
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

}