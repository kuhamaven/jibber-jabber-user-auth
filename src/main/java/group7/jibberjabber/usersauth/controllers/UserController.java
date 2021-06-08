package group7.jibberjabber.usersauth.controllers;

import group7.jibberjabber.usersauth.dtos.user.ReduceUserDto;
import group7.jibberjabber.usersauth.dtos.user.UserListingDto;
import group7.jibberjabber.usersauth.dtos.user.UserRegisterDto;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ReduceUserDto getUserModel(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @GetMapping()
    public UserListingDto getUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/available/{username}")
    public Boolean isUsernameAvailable(@PathVariable String username) {
        return !this.userService.usernameExists(username);
    }

    @GetMapping("/available/{email}")
    public Boolean isEmailAvailable(@PathVariable String email) {
        return !this.userService.emailExists(email);
    }

    @PostMapping("/register")
    public ReduceUserDto createUser(@RequestBody UserRegisterDto userDto) {
        return userService.registerUser(userDto);
    }

}
