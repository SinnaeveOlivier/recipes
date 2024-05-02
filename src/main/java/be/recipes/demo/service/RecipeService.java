package be.recipes.demo.service;


import be.recipes.demo.repository.RecipeRepository;
import be.recipes.demo.repository.entity.IngredientEntity;
import be.recipes.demo.repository.entity.RecipeEntity;
import be.recipes.demo.service.model.request.CreateIngredientRequest;
import be.recipes.demo.service.model.request.CreateRecipeRequest;
import be.recipes.demo.service.model.request.UpdateRecipeRequest;
import be.recipes.demo.service.model.response.RecipeDetailVO;
import be.recipes.demo.service.model.response.RecipeOverviewVO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class RecipeService {

    public static final String RECIPE_NOT_FOUND = "Recipe with id '%s' not found.";

    private final RecipeRepository recipeRepository;

    public List<RecipeOverviewVO> getOverview() {
        return StreamSupport.stream(recipeRepository.findAll().spliterator(), false)
                .map(RecipeOverviewVO::from)
                .sorted(Comparator.comparing(RecipeOverviewVO::getName))
                .collect(Collectors.toList());
    }

    public RecipeDetailVO getDetail(UUID recipeId) {
        return recipeRepository.findById(recipeId)
                .map(RecipeDetailVO::from)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RECIPE_NOT_FOUND, recipeId)));
    }

    public RecipeDetailVO saveRecipe(CreateRecipeRequest request) {
        RecipeEntity detail = request.toDomain();
        return RecipeDetailVO.from(recipeRepository.save(detail));
    }

    public void updateRecipe(UUID recipeId, UpdateRecipeRequest request) {
        RecipeEntity detail = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RECIPE_NOT_FOUND, recipeId)));

        detail.update(request.getName(), request.getDescription(), request.getCookingInstructions());
        request.toDomain(recipeId).getIngredients().forEach(detail::updateOrCreateIngredient);

        recipeRepository.save(detail);
    }

    public void deleteRecipe(UUID recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public void addIngredient(UUID recipeId, CreateIngredientRequest request) {
        RecipeEntity detail = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RECIPE_NOT_FOUND, recipeId)));

        IngredientEntity ingredient = request.toDomain();
        detail.addIngredient(ingredient);
        recipeRepository.save(detail);
    }

    public void deleteIngredient(UUID recipeId, UUID id) {
        RecipeEntity detail = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RECIPE_NOT_FOUND, recipeId)));

        detail.removeIngredient(id);
        recipeRepository.save(detail);
    }
}
