package group7.jibberjabber.usersauth.services;

import group7.jibberjabber.usersauth.dtos.user.ReduceUserDto;
import group7.jibberjabber.usersauth.dtos.user.UserListingDto;
import group7.jibberjabber.usersauth.dtos.user.UserRegisterDto;
import group7.jibberjabber.usersauth.exceptions.BadRequestException;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

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

    public List<User> findAllByEmail(String email) {
        return this.userRepository.findAllByEmail(email);
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
        if (StringUtils.isEmpty(userDto.getFirstName())) return false;
        if (StringUtils.isEmpty(userDto.getLastName())) return false;
        if (StringUtils.isEmpty(userDto.getDni())) return false;
        if (StringUtils.isEmpty(userDto.getUsername())) return false;
        if (userDto.getBirthDate() == null || userDto.getBirthDate().isAfter(LocalDate.now())) return false;
        if (StringUtils.isEmpty(userDto.getPassword()) || !userDto.getPassword().matches(passwordRegex))
            return false;
        return (!StringUtils.isEmpty(userDto.getEmail()) && userDto.getEmail().matches(emailRegex));
    }

    public ReduceUserDto registerUser(UserRegisterDto userRegisterDto) {
        if (!validUser(userRegisterDto))
            throw new BadRequestException("¡Por favor, verifique los datos enviados!");
        if (usernameExists(userRegisterDto.getUsername()))
            throw new BadRequestException("¡El nombre de usuario ingresado no está disponible!");
        User savedUser = saveUser(userRegisterDto);
        return ReduceUserDto.toDto(savedUser);
    }

    public boolean usernameExists(String userName) {
        return this.userRepository.findByUsername(userName).isPresent();
    }

    public boolean userExists(String id) {
        return this.userRepository.findById(id).isPresent();
    }

}
