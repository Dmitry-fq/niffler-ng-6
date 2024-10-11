package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UdUserRepository {

    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findUserById(UUID id);

    void addIncomeInvitation(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);
}
