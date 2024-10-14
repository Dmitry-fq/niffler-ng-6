package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.UUID;

public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("user")
        AuthUserJson user,

        @JsonProperty("authority")
        String authority) {

    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        final AuthUserEntity user = entity.getUser();

        return new AuthorityJson(
                entity.getId(),
                new AuthUserJson(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getEnabled(),
                        user.getAccountNonExpired(),
                        user.getAccountNonLocked(),
                        user.getCredentialsNonExpired()
                ),
                entity.getAuthority().name()
        );
    }
}
