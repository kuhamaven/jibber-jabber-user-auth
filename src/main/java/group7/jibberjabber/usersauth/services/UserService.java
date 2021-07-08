package group7.jibberjabber.usersauth.services;

import group7.jibberjabber.usersauth.dtos.user.*;
import group7.jibberjabber.usersauth.exceptions.BadRequestException;
import group7.jibberjabber.usersauth.models.Following;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.repositories.FollowingRepository;
import group7.jibberjabber.usersauth.repositories.UserRepository;
import group7.jibberjabber.usersauth.security.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class
UserService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionUtils sessionUtils;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, FollowingRepository followingRepository) {
        this.userRepository = userRepository;
        this.followingRepository = followingRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        sessionUtils = new SessionUtils(userRepository);
    }

    public ReduceUserDto findUserById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new BadRequestException("User with id: " + id + " not found!"));
        return ReduceUserDto.toDto(user);
    }

    public UserListingDto findAll() {
        List<User> users = this.userRepository.findAll();
        return UserListingDto.toDto(users);
    }

    public User saveUser(UserRegisterDto userDto) {
        User user = User.fromDto(userDto);
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return this.userRepository.save(user);
    }

    public boolean validUser(UserRegisterDto userDto) {
        String passwordRegex = "^(?=.*\\d)(?=.*[a-zA-Z])([a-zA-Z0-9]+){8,}$";
        String emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (userDto.getUsername().isEmpty()) return false;
        if (!userDto.getPassword().matches(passwordRegex)) return false;
        return (userDto.getEmail().matches(emailRegex));
    }

    public ReduceUserDto registerUser(UserRegisterDto userRegisterDto) {
        if (!validUser(userRegisterDto))
            throw new BadRequestException("¡Por favor, verifique los datos enviados!");
        if (usernameExists(userRegisterDto.getUsername()))
            throw new BadRequestException("¡El nombre de usuario ingresado no está disponible!");
        if (emailExists(userRegisterDto.getEmail()))
            throw new BadRequestException("¡El email ingresado ya está en uso!");
        User savedUser = saveUser(userRegisterDto);
        return ReduceUserDto.toDto(savedUser);
    }

    public boolean usernameExists(String userName) {
        return this.userRepository.findByUsername(userName).isPresent();
    }

    public boolean emailExists(String email){return this.userRepository.findByEmail(email).isPresent(); }

    public boolean userExists(String id) {
        return this.userRepository.findById(id).isPresent();
    }

    public LoggedUserDto getLogged() {
        return new LoggedUserDto(sessionUtils.getCurrentUser().getId(),sessionUtils.getCurrentUser().getUsername());
    }

    public ReduceUserDto updateUser(UpdateUserDto updateUserDto){
        User user = sessionUtils.getCurrentUser();
        if(!ObjectUtils.isEmpty(updateUserDto.getBio()))user.setBio(updateUserDto.getBio());
        if(!ObjectUtils.isEmpty(updateUserDto.getNick()))user.setNick(updateUserDto.getNick());
        if(!ObjectUtils.isEmpty(updateUserDto.getPassword())){
            String passwordRegex = "^(?=.*\\d)(?=.*[a-zA-Z])([a-zA-Z0-9]+){8,}$";
            if(updateUserDto.getPassword().matches(passwordRegex)) {
                user.setPassword(bCryptPasswordEncoder.encode(updateUserDto.getPassword()));
            }
            else throw new BadRequestException("Invalid password!");
        }
        user = this.userRepository.save(user);
        return ReduceUserDto.toDto(user);
    }

    public ReduceUserDto follow(String id) {
        User user = sessionUtils.getCurrentUser();
        if(followingRepository.findByFollowerIdAndFollowingId(user.getId(),id).isPresent()) return ReduceUserDto.toDto(user);
        followingRepository.save(new Following(user.getId(),id));
        return ReduceUserDto.toDto(user);
    }

    public ReduceUserDto unfollow(String id) {
        User user = sessionUtils.getCurrentUser();
        if(followingRepository.findByFollowerIdAndFollowingId(user.getId(),id).isEmpty()) return ReduceUserDto.toDto(user);
        followingRepository.delete(followingRepository.findByFollowerIdAndFollowingId(user.getId(),id).get());
        return ReduceUserDto.toDto(user);
    }

    public FollowedDto followed() {
        User user = sessionUtils.getCurrentUser();
        List<Following> following = followingRepository.findAllByFollowerId(user.getId());
        List<String> list = following.stream().map(Following::getFollowingId).collect(Collectors.toList());
        list.add(user.getId());
        FollowedDto followed = new FollowedDto(list);
        return followed;
    }

    public ReduceUserDto search(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(ReduceUserDto::toDto).orElseGet(ReduceUserDto::new);
    }

    public UserListingDto wildcard(String search) {
        return UserListingDto.toDto(userRepository.findAllByUsernameContaining(search));
    }
}
