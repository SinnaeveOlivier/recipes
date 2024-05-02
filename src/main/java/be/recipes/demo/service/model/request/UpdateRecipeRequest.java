package be.recipes.demo.service.model.request;

import be.recipes.demo.repository.entity.RecipeEntity;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
public class UpdateRecipeRequest {

    String name;
    String description;
    String cookingInstructions;
    List<UpdateIngredientRequest> ingredients;

    public RecipeEntity toDomain(UUID id) {
        RecipeEntity recipe = RecipeEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .cookingInstructions(cookingInstructions)
                .lastUpdatedDate(LocalDateTime.now())
                .ingredients(new ArrayList<>())
                .build();

        ingredients.forEach(ingredient -> recipe.addIngredient(ingredient.toDomain()));
        return recipe;
    }

}
