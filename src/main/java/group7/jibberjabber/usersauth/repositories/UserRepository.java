package group7.jibberjabber.usersauth.repositories;

import group7.jibberjabber.usersauth.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    List<User> findAllByUsernameContaining(String search);
}
