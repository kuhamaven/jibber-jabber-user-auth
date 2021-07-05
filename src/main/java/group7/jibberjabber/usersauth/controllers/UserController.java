package group7.jibberjabber.usersauth.controllers;

import group7.jibberjabber.usersauth.dtos.user.*;
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

    @GetMapping("/currentUser")
    public LoggedUserDto getLogged(){
        return userService.getLogged();
    }

    @PutMapping("/update")
    public ReduceUserDto updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }

    @PutMapping("/follow/{id}")
    public ReduceUserDto follow(@PathVariable String id){
        return userService.follow(id);
    }

    @PutMapping("/unfollow/{id}")
    public ReduceUserDto unfollow(@PathVariable String id){
        return userService.unfollow(id);
    }

    @GetMapping("/followed")
    public FollowedDto followed(){
        return userService.followed();
    }

    @GetMapping("/search/{username}")
    public ReduceUserDto search(@PathVariable String username){
        return userService.search(username);
    }

    @GetMapping("/wildcard/{search}")
    public List<String> wildcard(@PathVariable String search){
        return userService.wildcard(search);
    }
}
