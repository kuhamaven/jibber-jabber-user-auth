package group7.jibberjabber.usersauth.security;

import group7.jibberjabber.usersauth.exceptions.BadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();

        if (cookies==null || cookies.length<1) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = authenticate(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken authenticate(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if(cookies == null || cookies.length<1) return null;

        Cookie sessionCookie = null;
        for( Cookie cookie : cookies ){
            if(SecurityConstants.HEADER_NAME.equals(cookie.getName())){
                sessionCookie = cookie;
                break;
            }
        }

        if(sessionCookie==null || sessionCookie.getValue().isEmpty()){
                throw new BadRequestException("Invalid token");
        }

        Claims user = Jwts.parser()
                .setSigningKey(SecurityConstants.KEY.getBytes())
                .parseClaimsJws(sessionCookie.getValue())
                .getBody();

        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }else{
            return  null;
        }
    }
}
