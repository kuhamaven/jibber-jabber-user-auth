package group7.jibberjabber.usersauth.models;


import group7.jibberjabber.usersauth.dtos.user.UserRegisterDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(nullable = false)
    private String id;

    @Setter
    @Column
    private boolean active;

    @Setter
    @Column
    private String firstName;

    @Setter
    @Column
    private String lastName;

    @Setter
    @Column
    private String email;

    @Setter
    @Column
    private String dni;

    @Setter
    @Column
    private String username;

    @Setter
    @Column
    private String password;

    @Setter
    @Column
    private LocalDate birthDate;

    public User() {
        active = true;
    }

    public User(String firstName, String lastName, String email, String dni, String username, String password,LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
        this.username = username;
        this.password = password;
        this.active = true;
        this.birthDate = birthDate;
    }

    public static User fromDto(UserRegisterDto userDto){
        return new User(userDto.getFirstName(),userDto.getLastName(),userDto.getEmail(),userDto.getDni(),userDto.getUsername(),userDto.getPassword(),userDto.getBirthDate());
    }
}
