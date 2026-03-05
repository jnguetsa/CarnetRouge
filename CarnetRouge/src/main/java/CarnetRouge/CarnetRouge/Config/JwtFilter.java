package CarnetRouge.CarnetRouge.Config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 🔍 DEBUG
        System.out.println("=== JWT FILTER ===");
        System.out.println("URL : " + request.getRequestURI());
        System.out.println("Cookies : " + (request.getCookies() == null ? "NULL" : Arrays.toString(request.getCookies())));

        String token = extractTokenFromCookie(request);
        System.out.println("Token extrait : " + (token == null ? "NULL" : token.substring(0, 20) + "..."));
        System.out.println("==================");

        // ✅ Pas de token → on passe au filtre suivant
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String email = jwtService.extractUsername(token);
            System.out.println("Email extrait du token : " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(token, userDetails)) {
                    System.out.println("✅ Token valide pour : " + email);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("✅ Authentification réussie pour : " + email);
                    System.out.println("Authorities : " + authToken.getAuthorities());
                } else {
                    System.out.println("❌ Token invalide !");
                }
            }
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token expiré !");
            response.sendRedirect("/login?expired=true");
            return;
        } catch (Exception e) {
            System.out.println("❌ Exception dans JwtFilter : " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // ✅ Un seul appel à filterChain.doFilter()
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "JWT_TOKEN".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}