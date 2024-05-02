package be.recipes.demo.service.model.request;

import be.recipes.demo.repository.entity.RecipeEntity;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Value
public class CreateRecipeRequest {

    String name;
    String description;
    String cookingInstructions;
    List<CreateIngredientRequest> ingredients;

    public RecipeEntity toDomain() {
        RecipeEntity recipe = RecipeEntity.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .cookingInstructions(cookingInstructions)
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        ingredients.forEach(ingredient -> recipe.addIngredient(ingredient.toDomain()));
        return recipe;
    }

}
