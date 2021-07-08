package group7.jibberjabber.usersauth.repositories;

import group7.jibberjabber.usersauth.models.Following;
import group7.jibberjabber.usersauth.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends PagingAndSortingRepository<Following, String> {

    Optional<Following> findByFollowerIdAndFollowingId(String followerId,String followingId);

    List<Following> findAllByFollowerId(String followerId);
}
