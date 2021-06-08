package group7.jibberjabber.usersauth.security;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtils {

    public static String getTokenUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((DefaultClaims)authentication.getPrincipal()).get("sub").toString();
    }
}
