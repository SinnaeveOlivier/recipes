package be.recipes.demo.web.model;

import be.recipes.demo.service.model.response.IngredientVO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Ingredient {

    UUID id;
    String name;
    String quantity;

    public static Ingredient from(IngredientVO ingredientVO) {
        return Ingredient.builder()
                .id(ingredientVO.getId())
                .name(ingredientVO.getName())
                .quantity(ingredientVO.getQuantity())
                .build();
    }
}
