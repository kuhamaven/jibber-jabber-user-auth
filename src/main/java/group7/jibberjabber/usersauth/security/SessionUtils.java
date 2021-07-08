package group7.jibberjabber.usersauth.security;

import group7.jibberjabber.usersauth.exceptions.BadRequestException;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.repositories.UserRepository;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtils {

    private final UserRepository userRepository;

    @Autowired
    public SessionUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((DefaultClaims)authentication.getPrincipal()).get("sub").toString();
        return userRepository.findByUsername(username).orElseThrow(()-> new BadRequestException("Invalid Token"));
    }
}
