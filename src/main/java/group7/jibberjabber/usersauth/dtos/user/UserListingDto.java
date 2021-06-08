package group7.jibberjabber.usersauth.dtos.user;

import group7.jibberjabber.usersauth.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListingDto {

    List<ReduceUserDto> users;

    public static UserListingDto toDto (List<User> users){
        return new UserListingDto(users.stream().map(ReduceUserDto::toDto).collect(Collectors.toList()));
    }
}
