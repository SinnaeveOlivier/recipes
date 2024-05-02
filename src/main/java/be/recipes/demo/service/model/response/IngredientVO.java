package be.recipes.demo.service.model.response;

import be.recipes.demo.repository.entity.IngredientEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class IngredientVO {

    UUID id;
    String name;
    String quantity;

    public static IngredientVO from(IngredientEntity ingredientEntity) {
        return IngredientVO.builder()
                .id(ingredientEntity.getId())
                .name(ingredientEntity.getName())
                .quantity(ingredientEntity.getQuantity())
                .build();
    }
}
