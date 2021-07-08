package group7.jibberjabber.usersauth.models;


import group7.jibberjabber.usersauth.dtos.user.UserRegisterDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false)
    private String id;

    private boolean active;

    private String email;

    private String username;

    private String password;

    private String nick;

    private String bio;

    public User() {
        active = true;
    }

    public User(String email, String username, String password, String nick) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = true;
        this.nick = nick;
        this.bio = "";
//        this.following = new HashSet<>();
    }

    public static User fromDto(UserRegisterDto userDto){
        return new User(userDto.getEmail(),userDto.getUsername(),userDto.getPassword(),userDto.getNick());
    }
}
