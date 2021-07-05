package group7.jibberjabber.usersauth.services;

import group7.jibberjabber.usersauth.dtos.user.*;
import group7.jibberjabber.usersauth.exceptions.BadRequestException;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.repositories.UserRepository;
import group7.jibberjabber.usersauth.security.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class
UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        String user = SessionUtils.getTokenUsername();
        return new LoggedUserDto(this.userRepository.findByUsername(user).get().getId(),user);
    }

    public ReduceUserDto updateUser(UpdateUserDto updateUserDto){
        User user = this.userRepository.findByUsername(SessionUtils.getTokenUsername()).get();
        if(!updateUserDto.getBio().isEmpty())user.setBio(updateUserDto.getBio());
        if(!updateUserDto.getNick().isEmpty())user.setNick(updateUserDto.getNick());
        user = this.userRepository.save(user);
        return ReduceUserDto.toDto(user);
    }

    public ReduceUserDto follow(String id) {
        User user = this.userRepository.findByUsername(SessionUtils.getTokenUsername()).get();
        User userToFollow = this.userRepository.findById(id).get();
        if(user.getFollowing().contains(userToFollow)) return ReduceUserDto.toDto(user);
        Set<User> following = user.getFollowing();
        following.add(userToFollow);
        user.setFollowing(following);
        return ReduceUserDto.toDto(userRepository.save(user));
    }

    public ReduceUserDto unfollow(String id) {
        User user = this.userRepository.findByUsername(SessionUtils.getTokenUsername()).get();
        User userToFollow = this.userRepository.findById(id).get();
        if(!user.getFollowing().contains(userToFollow)) return ReduceUserDto.toDto(user);
        Set<User> following = user.getFollowing();
        following.remove(userToFollow);
        user.setFollowing(following);
        return ReduceUserDto.toDto(userRepository.save(user));
    }

    public FollowedDto followed() {
        User user = this.userRepository.findByUsername(SessionUtils.getTokenUsername()).get();
        List<String> list = user.getFollowing().stream().map(User::getId).collect(Collectors.toList());
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
