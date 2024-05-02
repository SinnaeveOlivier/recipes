package be.recipes.demo.repository.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientEntityTest {

    @Test
    void update() {
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime lastUpdatedDate = LocalDateTime.now();
        IngredientEntity entity = IngredientEntity.builder()
                .id(UUID.randomUUID())
                .name("name")
                .quantity("quantity")
                .creationDate(creationDate)
                .lastUpdatedDate(lastUpdatedDate)
                .build();

        entity.update("other name", "other quantity");

        assertThat(entity.getName()).isEqualTo("other name");
        assertThat(entity.getQuantity()).isEqualTo("other quantity");
        assertThat(entity.getCreationDate()).isEqualTo(creationDate);
        assertThat(entity.getLastUpdatedDate()).isNotEqualTo(lastUpdatedDate);
    }
}