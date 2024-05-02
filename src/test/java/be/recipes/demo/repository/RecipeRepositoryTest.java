package be.recipes.demo.repository;

import be.recipes.demo.repository.entity.RecipeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;


    @Test
    public void testSaveAndFindAll() {
        // Create a recipe entity
        UUID recipeId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructies")
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        // Save the recipe entity to the repository
        recipeRepository.save(recipe);

        // Find the saved recipe by ID
        List<RecipeEntity> recipes = StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .toList();


        // Assert the recipes list
        assertThat(recipes).isNotEmpty();
        assertEquals(recipe.getId(), recipes.get(0).getId(), "Recipe ID should match");
        assertEquals(recipe.getName(), recipes.get(0).getName(), "Recipe name should match");
        assertEquals(recipe.getDescription(), recipes.get(0).getDescription(), "Recipe description should match");
    }

    @Test
    public void testSaveAndFindById() {
        // Create a recipe entity
        UUID recipeId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructies")
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        // Save the recipe entity to the repository
        recipeRepository.save(recipe);

        // Find the saved recipe by ID
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(recipe.getId());

        // Check if the recipe is present
        assertTrue(optionalRecipe.isPresent(), "Recipe should be present");

        // Get the saved recipe from optional
        RecipeEntity savedRecipe = optionalRecipe.get();

        // Assert the saved recipe's properties
        assertEquals(recipe.getId(), savedRecipe.getId(), "Recipe ID should match");
        assertEquals(recipe.getName(), savedRecipe.getName(), "Recipe name should match");
        assertEquals(recipe.getDescription(), savedRecipe.getDescription(), "Recipe description should match");
    }

    @Test
    public void testSaveAndUpdate() {
        // Create a recipe entity
        UUID recipeId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructies")
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        // Save the recipe entity to the repository
        recipeRepository.save(recipe);

        recipe.update("Test recipe 2", "Description 2", "Cooking instructions 2");

        // Update the recipe entity to the repository
        recipeRepository.save(recipe);

        RecipeEntity optionalRecipe = recipeRepository.findById(recipe.getId()).orElse(null);

        // Assert the saved recipe's properties
        assertThat(optionalRecipe).isNotNull();
        assertEquals(recipe.getId(), optionalRecipe.getId(), "Recipe ID should match");
        assertEquals(recipe.getName(), optionalRecipe.getName(), "Recipe name should match");
        assertEquals(recipe.getDescription(), optionalRecipe.getDescription(), "Recipe description should match");
    }

    @Test
    public void testSaveAndDeleteById() {
        // Create a recipe entity
        UUID recipeId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructies")
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        // Save the recipe entity to the repository
        recipeRepository.save(recipe);

        // Delete the saved recipe by ID
        recipeRepository.deleteById(recipeId);

        // Check if the recipe is present
        Optional<RecipeEntity> optionalRecipe = recipeRepository.findById(recipe.getId());
        assertFalse(optionalRecipe.isPresent(), "Recipe should not be present");
    }
}
