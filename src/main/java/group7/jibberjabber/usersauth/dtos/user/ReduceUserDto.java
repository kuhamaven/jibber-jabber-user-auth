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

    private String firstName;

    private String lastName;

    public static ReduceUserDto toDto(User user) {
        return new ReduceUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }
}
