package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipEntity;
import guru.qa.niffler.data.FriendshipStatus;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.projection.UserWithStatus;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.FriendshipStatus.FRIEND;
import static guru.qa.niffler.model.FriendshipStatus.INVITE_SENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UUID mainTestUserUuid = UUID.randomUUID();

    private final String mainTestUserName = "dima";

    private final UUID secondTestUserUuid = UUID.randomUUID();

    private final String secondTestUserName = "barsik";

    private final UUID thirdTestUserUuid = UUID.randomUUID();

    private final String thirdTestUserName = "emma";

    private final String notExistingUser = "not_existing_user";

    private UserService userService;

    private UserEntity mainTestUser;

    private UserEntity secondTestUser;

    private UserEntity thirdTestUser;

    @BeforeEach
    void init() {
        mainTestUser = new UserEntity();
        mainTestUser.setId(mainTestUserUuid);
        mainTestUser.setUsername(mainTestUserName);
        mainTestUser.setCurrency(CurrencyValues.RUB);

        secondTestUser = new UserEntity();
        secondTestUser.setId(secondTestUserUuid);
        secondTestUser.setUsername(secondTestUserName);
        secondTestUser.setCurrency(CurrencyValues.RUB);

        thirdTestUser = new UserEntity();
        thirdTestUser.setId(thirdTestUserUuid);
        thirdTestUser.setUsername(thirdTestUserName);
        thirdTestUser.setCurrency(CurrencyValues.RUB);
    }

    @ValueSource(strings = {
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAACwVBMVEUanW4am2wBY0QYmWsMfFYBZEQAAQBHcEz+" +
                    "/v4AAAAZm20anW4bnW4IdFAanW4anW4BZUQbnm8bnm8anW4anG4VflganW4anW4Jd1Ibnm8EbEoDaUcbnW4anW4anW4bnm8anW4anW4" +
                    "anG4BZUQWlGcHKR0anG0anW4FbksanG4anW4FbEobnm4anW4anW4anW4anW4anW4Zm2wanG0ZmmwZm20BY0MZmmwanW4anG4anG0cnm9B" +
                    "roj0+vj9/v4hoXIAYkMfoHEMfFYLe1QWlGgTjWIQh14XlWgNf1gNf1gAYUIAYkIAYUIAYUIAYUIKeFMAYUIEakgAYUISimAAYUIAYEEAYUE" +
                    "AYUIAYUIAYUIBCwcanW4MSjQHLB8BBwUJOCcNTzcSbUwVe1YanW4AAwIPWj8Sakvj8+0LQC0ZmmwAY0MAY0MAYkNdupglonVovp9RtJFbuZg1" +
                    "qX8Zm2yAyK7f8er3+/p9x6yk2MUDaUi038+Fy7IHck6w3c0KeFMGb0wEakllvZ4IdVAGcE0Le1UKeVMbnm8AAAD///8AYUIanW4Zm20amGsCEAs" +
                    "GJhoMfFYZmmwOVj0EHBQYj2QBBgQSbEwYjGIam2wUd1MYkmYCDQkFHhUOVDsJdVEJNSUZlWgSbU0Zk2cVkmYABANqv6Hs9vKr28pTtpLn9fBeupn+/" +
                    "v46q4Mdn3D6/PsDaUcPhFwBZEQIc08AYkIYmWsVfliU0bsAAQALQS0ZlWkOUjkYjmMUeFSb1MB5xqoBCQYDFg8KPSsHKx4Zl2oYkGV3xakanG4NUDgcnnC" +
                    "OzrcIMiMzqH5IsYv1+vmHzLNauJbF5tq84tRUtpPX7eWg1sJDr4gOgVn6/fyCyrArpXlwwqWm2cfa7+goo3fB5Ng9rIUjoXQhoHOe1sLT7ONLso0OgVrt9/P9/v2" +
                    "+49XI59zW7eUKeVOYug0QAAAAhnRSTlP+/q/+/gX+AP7+/vYU/QO9kAb7sv7+EcH+helU03EZM/0BIQj+/vv3E+2o/rBEFZlayYYr4Aj9RPrrlv7+/v7+VP71Bf7Zs" +
                    "PXhfOkTkvUd/ob+OP58/uHz2bD+0P7+/v7+/v7P/v7+/v66r5CQ/v7+/v7+uv7+/v7+kv7+Hf44hpL+HYY4/hahmy4AAAMTSURBVEjHY2BHBqbmRsUmagwMbVCkZpJp" +
                    "ZG6KooQBiW1sZQlXCkdtDJZWxlg1KGlZMDBg08DQZqGlhKGBT5a7DQ/gluVD1SAmDDEMuw0gJCyGrEEumoGQBoZkOYQGHpk2IoAMD0yDID/cMDw2tDHwC0I1SDIQp4FBEq" +
                    "JBXgirC7oXTpvcgyokJA/SoCjehmkDV+K8XrZ2zri+iYeR7RNXBGrQVcDUMGMSZzsnBO2bw4jQoKAL1KCD4ZiuOdvakUDfTISUDjuDnjoDmg1dCRCzo/aDXAVkzp4PV6Kux2DA" +
                    "gK5hARtQFdsBUeY2kWkTZ4H0xiO8YcCgj+6gQ71AZ8zaDeXNPNjePmkLQlafwRDdhlSgmVNE4YI9fTE9SAFlyKCJbsNsoAU7kfjTUWQ1GRjQbJicAgz+Hdhjuo2BAT0VMLTNAPo4F" +
                    "ipivxybBjQwFeii7RDm40drV2NEEoYNiUAbMsDc6mccHA8JOykJqEEVzG3s4OBY4UjQSfMXt7cvXghirXna0bHqHkEnMagC4+EYWOQ5B0dHLkEnMaS1c87bBRZZBtSwhKCT2o6eXNAFYV" +
                    "3o6OhYSdhJcGS9lINjlQ2GDd04NawEhtJS9FDiYnBHtm/1OQT7/kugi26hu8idwRWhfU25re1yGPdBLUcHh90V9IhzZXBD8IChwnGmxB7MtnkBDCKOCoy05MbgibDu4lqgIzpWXD5y8+zdayD" +
                    "mpTsYYejJUOeC8HR+GdBUBLpdhJG8XVoYnD2QQqmgFElD1Q3M/ODhzMBez4wUrHnX0yGq7c4XWmNmIOZmYLlU44TiyOOn927dc2LJKazFp1MTqGytXERsYbyoAVwYa0hNaCMKTJDSgBT30rwixN" +
                    "ggwisNq4EEJLoIa+iSEIBXWaxMc7sJuad7LhMrolJUztJWwW+DijaTMnK1y5pj9oQLtwauq2bZrGgtAQevdetxuKt7/TovB8ymg7eP7wb/4M3oNmwO9t/g6+ONtXHiFxAWuilw4ysWln4oat0YuCk0" +
                    "LMAPR2sG6JWgkMiI8E44CI+IDAliRVECAE4WhZg/rX3CAAAAAElFTkSuQmCC",
            ""
    })
    @ParameterizedTest
    void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(Optional.of(mainTestUser));

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        userService = new UserService(userRepository);

        final String photoForTest = photo.isEmpty() ? null : photo;

        final UserJson toBeUpdated = new UserJson(
                null,
                mainTestUserName,
                "Test",
                "TestSurname",
                "Test TestSurname",
                CurrencyValues.USD,
                photoForTest,
                null,
                null
        );
        final UserJson result = userService.update(toBeUpdated);
        assertEquals(mainTestUserUuid, result.id());
        assertEquals("Test TestSurname", result.fullname());
        assertEquals(CurrencyValues.USD, result.currency());
        assertEquals(photoForTest, result.photo());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(Optional.empty());

        userService = new UserService(userRepository);

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getRequiredUser(notExistingUser));
        assertEquals(
                "Can`t find user by username: '" + notExistingUser + "'",
                exception.getMessage()
        );
    }

    @Test
    void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
        when(userRepository.findByUsernameNot(eq(mainTestUserName)))
                .thenReturn(getMockUsersMappingFromDb());

        userService = new UserService(userRepository);

        final List<UserJsonBulk> users = userService.allUsers(mainTestUserName, null);
        assertEquals(2, users.size());
        final UserJsonBulk invitation = users.stream()
                                             .filter(u -> u.friendshipStatus() == INVITE_SENT)
                                             .findFirst()
                                             .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));

        final UserJsonBulk friend = users.stream()
                                         .filter(u -> u.friendshipStatus() == null)
                                         .findFirst()
                                         .orElseThrow(() -> new AssertionError("user without status not found"));

        assertEquals(secondTestUserName, invitation.username());
        assertEquals(thirdTestUserName, friend.username());
    }

    @Test
    void allUsersShouldCallCorrectRepositoryMethod(@Mock UserRepository userRepository) {
        userService = new UserService(userRepository);
        userService.allUsers(mainTestUserName, "123");

        verify(userRepository, times(1))
                .findByUsernameNot(eq(mainTestUserName), eq("123"));
    }

    @Test
    void getCurrentUserShouldReturnCorrectUserJson(@Mock UserRepository userRepository) {
        final UserEntity userEntity = getUserEntity(mainTestUserName);

        when(userRepository.findByUsername(eq(userEntity.getUsername())))
                .thenReturn(Optional.of(userEntity));

        final UserService userService = new UserService(userRepository);
        final UserJson result = userService.getCurrentUser(userEntity.getUsername());

        assertEquals(userEntity.getId(), result.id());
        assertEquals(userEntity.getUsername(), result.username());
        assertEquals(userEntity.getCurrency(), result.currency());
        assertEquals(userEntity.getFirstname(), result.firstname());
        assertEquals(userEntity.getSurname(), result.surname());
        assertEquals(userEntity.getFullname(), result.fullname());
    }

    @Test
    void getCurrentUserShouldReturnDefaultUserJsonIfNotFoundRecord(@Mock UserRepository userRepository) {
        final String username = "username";
        when(userRepository.findByUsername(eq(username)))
                .thenReturn(Optional.empty());

        final UserService userService = new UserService(userRepository);
        final UserJson result = userService.getCurrentUser(username);

        assertNull(result.id());
        assertEquals(username, result.username());
        assertEquals(CurrencyValues.RUB, result.currency());
        assertNull(result.firstname());
        assertNull(result.surname());
        assertNull(result.fullname());
    }

    @Test
    void friendsShouldCallCorrectRepositoryMethod(@Mock UserRepository userRepository) {
        final UserEntity user = getUserEntity(mainTestUserName);
        when(userRepository.findByUsername(eq(user.getUsername())))
                .thenReturn(Optional.of(user));

        userService = new UserService(userRepository);
        userService.friends(user.getUsername(), "123");

        verify(userRepository, times(1))
                .findFriends(eq(user), eq("123"));
    }

    @Test
    void createFriendshipRequestShouldCreateFriendshipRequest(@Mock UserRepository userRepository) {
        UserEntity currentUser = getUserEntity(mainTestUserName);
        UserEntity targetUser = getUserEntity(secondTestUserName);

        when(userRepository.findByUsername(eq(currentUser.getUsername())))
                .thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(eq(targetUser.getUsername())))
                .thenReturn(Optional.of(targetUser));

        userService = new UserService(userRepository);
        UserJson actualUserJson = userService.createFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());

        UserJson expectedUserJson = new UserJson(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getFirstname(),
                targetUser.getSurname(),
                targetUser.getFullname(),
                targetUser.getCurrency(),
                getBytesFromString(targetUser.getPhoto()),
                getBytesFromString(targetUser.getPhotoSmall()),
                INVITE_SENT
        );

        userJsonShouldBeCorrect(expectedUserJson, actualUserJson);
    }

    @Test
    void acceptFriendshipRequestShouldAcceptFriendshipRequest(@Mock UserRepository userRepository) {
        UserEntity currentUser = getUserEntity(mainTestUserName);
        UserEntity targetUser = getUserEntity(secondTestUserName);
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(targetUser);
        currentUser.setFriendshipAddressees(List.of(friendshipEntity));

        when(userRepository.findByUsername(eq(currentUser.getUsername())))
                .thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(eq(targetUser.getUsername())))
                .thenReturn(Optional.of(targetUser));

        userService = new UserService(userRepository);
        UserJson actualUserJson = userService.acceptFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());

        UserJson expectedUserJson = new UserJson(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getFirstname(),
                targetUser.getSurname(),
                targetUser.getFullname(),
                targetUser.getCurrency(),
                getBytesFromString(targetUser.getPhoto()),
                getBytesFromString(targetUser.getPhotoSmall()),
                FRIEND
        );

        userJsonShouldBeCorrect(expectedUserJson, actualUserJson);

    }

    @Test
    void declineFriendshipRequestShouldDeclineFriendshipRequest(@Mock UserRepository userRepository) {
        UserEntity currentUser = getUserEntity(mainTestUserName);
        UserEntity targetUser = getUserEntity(secondTestUserName);
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setAddressee(currentUser);
        targetUser.setFriendshipAddressees(List.of(friendshipEntity));

        when(userRepository.findByUsername(eq(currentUser.getUsername())))
                .thenReturn(Optional.of(currentUser));
        when(userRepository.findByUsername(eq(targetUser.getUsername())))
                .thenReturn(Optional.of(targetUser));

        userService = new UserService(userRepository);
        UserJson actualUserJson = userService.declineFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());

        UserJson expectedUserJson = new UserJson(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getFirstname(),
                targetUser.getSurname(),
                targetUser.getFullname(),
                targetUser.getCurrency(),
                getBytesFromString(targetUser.getPhoto()),
                getBytesFromString(targetUser.getPhotoSmall()),
                null
        );

        userJsonShouldBeCorrect(expectedUserJson, actualUserJson);
    }

    private List<UserWithStatus> getMockUsersMappingFromDb() {
        return List.of(
                new UserWithStatus(
                        secondTestUser.getId(),
                        secondTestUser.getUsername(),
                        secondTestUser.getCurrency(),
                        secondTestUser.getFullname(),
                        secondTestUser.getPhotoSmall(),
                        FriendshipStatus.PENDING
                ),
                new UserWithStatus(
                        thirdTestUser.getId(),
                        thirdTestUser.getUsername(),
                        thirdTestUser.getCurrency(),
                        thirdTestUser.getFullname(),
                        thirdTestUser.getPhotoSmall(),
                        FriendshipStatus.ACCEPTED
                )
        );
    }

    private UserEntity getUserEntity(String username) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);
        userEntity.setFirstname("firstname");
        userEntity.setSurname("surname");
        userEntity.setFullname("fullname");
        userEntity.setPhoto(new byte[12345]);
        userEntity.setPhotoSmall(new byte[123]);
        userEntity.setFriendshipRequests(new ArrayList<>());
        userEntity.setFriendshipAddressees(new ArrayList<>());

        return userEntity;
    }

    private String getBytesFromString(byte[] bytes) {
        return bytes != null && bytes.length > 0 ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

    private void userJsonShouldBeCorrect(UserJson expectedUserJson, UserJson actualUserJson) {
        assertEquals(expectedUserJson.id(), actualUserJson.id());
        assertEquals(expectedUserJson.username(), actualUserJson.username());
        assertEquals(expectedUserJson.currency(), actualUserJson.currency());
        assertEquals(expectedUserJson.firstname(), actualUserJson.firstname());
        assertEquals(expectedUserJson.surname(), actualUserJson.surname());
        assertEquals(expectedUserJson.fullname(), actualUserJson.fullname());
        assertEquals(expectedUserJson.photo(), actualUserJson.photo());
        assertEquals(expectedUserJson.photoSmall(), actualUserJson.photoSmall());
        assertEquals(expectedUserJson.friendshipStatus(), actualUserJson.friendshipStatus());
    }
}