package be.recipes.demo.web.model;

import be.recipes.demo.service.model.response.RecipeDetailVO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class RecipeDetail {

    UUID id;
    String name;
    String description;
    String cookingInstructions;
    List<Ingredient> ingredients;

    public static RecipeDetail from(RecipeDetailVO detail) {
        return RecipeDetail.builder()
                .id(detail.getId())
                .name(detail.getName())
                .description(detail.getDescription())
                .cookingInstructions(detail.getCookingInstructions())
                .ingredients(detail.getIngredients().stream().map(Ingredient::from).toList())
                .build();
    }
}
