package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthorityJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder(setterPrefix = "set")
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityEntity implements Serializable {

    private UUID id;

    private AuthUserEntity user;

    private Authority authority;

    public static AuthorityEntity fromJson(AuthorityJson json) {
        AuthorityEntity authUserEntity = new AuthorityEntity();
        authUserEntity.setId(json.id());
        authUserEntity.setUser(
                AuthUserEntity.fromJson(
                        json.user()
                )
        );
        authUserEntity.setAuthority(Authority.valueOf(json.authority()));

        return authUserEntity;
    }
}
