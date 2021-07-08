package group7.jibberjabber.usersauth.controllers;

import group7.jibberjabber.usersauth.dtos.user.*;
import group7.jibberjabber.usersauth.models.User;
import group7.jibberjabber.usersauth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ReduceUserDto getUserModel(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @GetMapping("/users")
    public UserListingDto getUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/users/available/{username}")
    public Boolean isUsernameAvailable(@PathVariable String username) {
        return !this.userService.usernameExists(username);
    }

    @GetMapping("/users/available/{email}")
    public Boolean isEmailAvailable(@PathVariable String email) {
        return !this.userService.emailExists(email);
    }

    @PostMapping("/users/register")
    public ReduceUserDto createUser(@RequestBody UserRegisterDto userDto) {
        return userService.registerUser(userDto);
    }

    @GetMapping("/users/currentUser")
    public LoggedUserDto getLogged(){
        return userService.getLogged();
    }

    @PutMapping("/users/update")
    public ReduceUserDto updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }

    @PutMapping("/users/follow/{id}")
    public ReduceUserDto follow(@PathVariable String id){
        return userService.follow(id);
    }

    @PutMapping("/users/unfollow/{id}")
    public ReduceUserDto unfollow(@PathVariable String id){
        return userService.unfollow(id);
    }

    @GetMapping("/users/followed")
    public FollowedDto followed(){
        return userService.followed();
    }

    @GetMapping("/users/search/{username}")
    public ReduceUserDto search(@PathVariable String username){
        return userService.search(username);
    }

    @GetMapping("/users/wildcard/{search}")
    public UserListingDto wildcard(@PathVariable String search){
        return userService.wildcard(search);
    }

    @GetMapping("/expiresession")
    public ReduceUserDto logout(HttpServletRequest req, HttpServletResponse res){
        return userService.logout(req, res);
    }
}
