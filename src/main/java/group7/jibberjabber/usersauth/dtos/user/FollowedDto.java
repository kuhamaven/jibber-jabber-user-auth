package group7.jibberjabber.usersauth.dtos.user;

import group7.jibberjabber.usersauth.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowedDto {

    private List<String> followed;

}
