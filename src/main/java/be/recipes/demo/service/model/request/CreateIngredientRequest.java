package be.recipes.demo.service.model.request;

import be.recipes.demo.repository.entity.IngredientEntity;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class CreateIngredientRequest {

    String name;
    String quantity;

    public IngredientEntity toDomain() {
        return IngredientEntity.builder()
                .id(UUID.randomUUID())
                .name(name)
                .quantity(quantity)
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();
    }

}
