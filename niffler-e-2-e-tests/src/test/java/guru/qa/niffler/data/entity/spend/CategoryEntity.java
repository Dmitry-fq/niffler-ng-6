package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CategoryJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity implements Serializable {
    private UUID id;

    private String name;

    private String username;

    private boolean archived;

    public static CategoryEntity fromJson(CategoryJson json) {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(json.id());
        ce.setName(json.name());
        ce.setUsername(json.username());
        ce.setArchived(json.archived());
        return ce;
    }
}
