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
public class UserEntity implements Serializable {

    private UUID id;

    private String username;

    private String password;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    public static UserEntity fromJson(AuthUserJson json) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(json.id());
        userEntity.setUsername(json.username());
        userEntity.setPassword(json.password());
        userEntity.setEnabled(json.enabled());
        userEntity.setAccountNonExpired(json.accountNonExpired());
        userEntity.setAccountNonLocked(json.accountNonLocked());
        userEntity.setCredentialsNonExpired(json.accountNonLocked());

        return userEntity;
    }
}
