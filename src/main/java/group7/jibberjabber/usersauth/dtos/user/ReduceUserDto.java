package group7.jibberjabber.usersauth.dtos.user;

import group7.jibberjabber.usersauth.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReduceUserDto {

    private String id;

    private String nick;

    private String username;

    private String email;

    private String bio;

    public static ReduceUserDto toDto(User user) {
        return new ReduceUserDto(user.getId(), user.getNick(), user.getUsername(), user.getEmail(), user.getBio());
    }
}
