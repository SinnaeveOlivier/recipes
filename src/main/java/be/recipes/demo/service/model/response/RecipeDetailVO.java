package be.recipes.demo.service.model.response;

import be.recipes.demo.repository.entity.RecipeEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class RecipeDetailVO {

    UUID id;
    String name;
    String description;
    String cookingInstructions;
    List<IngredientVO> ingredients;

    public static RecipeDetailVO from(RecipeEntity entity) {
        return RecipeDetailVO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .cookingInstructions(entity.getCookingInstructions())
                .ingredients(entity.getIngredients().stream()
                        .map(IngredientVO::from)
                        .sorted(Comparator.comparing(IngredientVO::getName))
                        .toList())
                .build();
    }

}
