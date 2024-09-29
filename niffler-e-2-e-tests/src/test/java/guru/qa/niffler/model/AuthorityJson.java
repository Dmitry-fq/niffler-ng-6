package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.UUID;

public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user")
        AuthUserJson user,

        @JsonProperty("authority")
        String authority) {

    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        final UserEntity user = entity.getUser();

        return new AuthorityJson(
                entity.getId(),
                new AuthUserJson(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.isEnabled(),
                        user.isAccountNonExpired(),
                        user.isAccountNonLocked(),
                        user.isCredentialsNonExpired()
                ),
                entity.getAuthority()
        );
    }
}