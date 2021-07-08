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
public class Following {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false)
    private String id;

    private String followerId;

    private String followingId;

    public Following() {
    }

    public Following(String followerId, String followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
