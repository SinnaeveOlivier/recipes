package be.recipes.demo.web;

import be.recipes.demo.service.RecipeService;
import be.recipes.demo.service.model.request.CreateIngredientRequest;
import be.recipes.demo.service.model.request.CreateRecipeRequest;
import be.recipes.demo.service.model.request.UpdateRecipeRequest;
import be.recipes.demo.web.model.RecipeDetail;
import be.recipes.demo.web.model.RecipeOverview;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService service;


    @GetMapping
    public List<RecipeOverview> getOverzicht() {
        return service.getOverview().stream()
                .map(RecipeOverview::from)
                .toList();
    }

    @GetMapping(value = "/{id}")
    public RecipeDetail getDetail(@PathVariable("id") UUID recipeId) {
        return RecipeDetail.from(service.getDetail(recipeId));
    }

    @PostMapping
    public RecipeDetail addRecipe(@RequestBody CreateRecipeRequest request) {
        return RecipeDetail.from(service.saveRecipe(request));
    }

    @PutMapping("/{id}")
    public void updateRecipe(@PathVariable("id") UUID recipeId,
                             @RequestBody UpdateRecipeRequest request) {
        service.updateRecipe(recipeId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable("id") UUID recipeId) {
        service.deleteRecipe(recipeId);
    }

    @PostMapping("/{recipeId}/ingredient")
    public void addIngredient(@PathVariable("recipeId") UUID recipeId,
                              @RequestBody CreateIngredientRequest request) {
        service.addIngredient(recipeId, request);
    }

    @DeleteMapping("/{recipeId}/ingredient/{id}")
    public void deleteIngredient(@PathVariable("recipeId") UUID recipeId,
                                 @PathVariable("id") UUID id) {
        service.deleteIngredient(recipeId, id);
    }

}

