package be.recipes.demo.web.model;

import be.recipes.demo.service.model.response.RecipeOverviewVO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeOverview {

    String id;
    String name;
    String description;
    String cookingInstructions;

    public static RecipeOverview from(RecipeOverviewVO recipeOverviewVO) {
        return RecipeOverview.builder()
                .id(recipeOverviewVO.getId().toString())
                .name(recipeOverviewVO.getName())
                .description(recipeOverviewVO.getDescription())
                .cookingInstructions(recipeOverviewVO.getCookingInstructions())
                .build();
    }
}
