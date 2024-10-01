package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.AuthUserJson;
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
public class AuthUserEntity implements Serializable {

    private UUID id;

    private String username;

    private String password;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    public static AuthUserEntity fromJson(AuthUserJson json) {
        AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setId(json.id());
        authUserEntity.setUsername(json.username());
        authUserEntity.setPassword(json.password());
        authUserEntity.setEnabled(json.enabled());
        authUserEntity.setAccountNonExpired(json.accountNonExpired());
        authUserEntity.setAccountNonLocked(json.accountNonLocked());
        authUserEntity.setCredentialsNonExpired(json.accountNonLocked());

        return authUserEntity;
    }
}
