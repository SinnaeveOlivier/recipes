package be.recipes.demo.service;

import be.recipes.demo.repository.RecipeRepository;
import be.recipes.demo.repository.entity.IngredientEntity;
import be.recipes.demo.repository.entity.RecipeEntity;
import be.recipes.demo.service.model.request.CreateIngredientRequest;
import be.recipes.demo.service.model.request.CreateRecipeRequest;
import be.recipes.demo.service.model.request.UpdateIngredientRequest;
import be.recipes.demo.service.model.request.UpdateRecipeRequest;
import be.recipes.demo.service.model.response.RecipeDetailVO;
import be.recipes.demo.service.model.response.RecipeOverviewVO;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Captor
    private ArgumentCaptor<RecipeEntity> saveRecipeCaptor;


    @Test
    void testGetOverview() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructies")
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        when(recipeRepository.findAll()).thenReturn(Collections.singletonList(recipe));

        // Call service method
        List<RecipeOverviewVO> overview = recipeService.getOverview();
        assertEquals(1, overview.size());
        assertEquals(recipeId, overview.get(0).getId());
        assertEquals(recipe.getName(), overview.get(0).getName());
        assertEquals(recipe.getDescription(), overview.get(0).getDescription());
        assertEquals(recipe.getCookingInstructions(), overview.get(0).getCookingInstructions());
    }

    @Test
    void testGetDetail() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .ingredients(Lists.newArrayList(IngredientEntity.builder()
                        .id(ingredientId)
                        .name("Ingredient name")
                        .quantity("2 Lepels")
                        .build()))
                .build();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Call service method
        RecipeDetailVO detailVO = recipeService.getDetail(recipeId);

        assertEquals(recipeId, detailVO.getId());
        assertEquals(recipe.getName(), detailVO.getName());
        assertEquals(recipe.getDescription(), detailVO.getDescription());
        assertEquals(recipe.getCookingInstructions(), detailVO.getCookingInstructions());
        assertEquals(recipe.getIngredients().size(), 1);
        assertEquals(recipe.getIngredients().get(0).getId(), ingredientId);
        assertEquals(recipe.getIngredients().get(0).getName(), "Ingredient name");
        assertEquals(recipe.getIngredients().get(0).getQuantity(), "2 Lepels");
    }

    @Test
    void testGetDetail_entityNotFound() {
        // Mock data
        UUID recipeId = UUID.randomUUID();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.getDetail(recipeId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Recipe with id '%s' not found.", recipeId));
    }

    @Test
    void testSaveRecipe() {
        // Mock data
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Test Recipe",
                "Test Recipe description",
                "Instruction",
                Lists.newArrayList(new CreateIngredientRequest(
                        "Ingredient",
                        "2 Lepels"))
        );

        when(recipeRepository.save(any())).thenReturn(request.toDomain());        // Call service method
        RecipeDetailVO detail = recipeService.saveRecipe(request);

        verify(recipeRepository).save(saveRecipeCaptor.capture());


        assertThat(detail.getId()).isNotNull();
        assertThat(detail.getIngredients().get(0).getId()).isNotNull();

        RecipeEntity recipeCaptorValue = saveRecipeCaptor.getValue();
        assertThat(request.toDomain())
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(UUID.class, LocalDateTime.class)
                .isEqualTo(recipeCaptorValue);
    }

    @Test
    void testUpdateRecipe() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Test Recipe",
                "Test Recipe description",
                "Instruction",
                Lists.newArrayList(new UpdateIngredientRequest(ingredientId, "ingredient name", "2 Lepels"))
        );

        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("recipe")
                .description("jk")
                .cookingInstructions("coo")
                .build();
        recipe.addIngredient(IngredientEntity.builder()
                .id(ingredientId)
                .name("name")
                .quantity("lepels")
                .build());

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(request.toDomain(recipeId));

        // Call service method
        recipeService.updateRecipe(recipeId, request);

        verify(recipeRepository).save(saveRecipeCaptor.capture());

        assertThat(request.toDomain(recipeId))
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(saveRecipeCaptor.getValue());
    }

    @Test
    void testUpdateRecipe_entityNotFound() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Test Recipe",
                "Test Recipe description",
                "Instruction",
                Lists.newArrayList()
        );

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.updateRecipe(recipeId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Recipe with id '%s' not found.", recipeId));
    }

    @Test
    void testDeleteRecipe() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        doNothing().when(recipeRepository).deleteById(recipeId);

        // Call service method
        recipeService.deleteRecipe(recipeId);

        // Verify that deleteById method of repository is called
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }

    @Test
    void testAddIngredient() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        CreateIngredientRequest request = new CreateIngredientRequest(
                "Test Ingredient",
                "2 lepels"
        );

        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .build();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Call service method
        recipeService.addIngredient(recipeId, request);

        // Verify that save method of repository is called
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    void testAddIngredient_entityNotFound() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        CreateIngredientRequest request = new CreateIngredientRequest(
                "Test Ingredient",
                "2 lepels"
        );

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.addIngredient(recipeId, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Recipe with id '%s' not found.", recipeId));
    }

    @Test
    void testDeleteIngredient() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        RecipeEntity recipe = RecipeEntity.builder()
                .id(recipeId)
                .name("Test recipe")
                .description("Description")
                .cookingInstructions("Cooking instructions")
                .build();
        recipe.addIngredient(IngredientEntity.builder()
                .id(ingredientId)
                .build());

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Call service method
        recipeService.deleteIngredient(recipeId, ingredientId);

        recipe.removeIngredient(ingredientId);
        // Verify that save method of repository is called
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void testDeleteIngredient_entityNotFound() {
        // Mock data
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Call service method
        assertThatThrownBy(() -> recipeService.deleteIngredient(recipeId, ingredientId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Recipe with id '%s' not found.", recipeId));
    }
}
