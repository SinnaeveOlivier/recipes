package be.recipes.demo.service.model.response;

import be.recipes.demo.repository.entity.RecipeEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RecipeOverviewVO {

    UUID id;
    String name;
    String description;
    String cookingInstructions;

    public static RecipeOverviewVO from(RecipeEntity entity) {
        return RecipeOverviewVO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .cookingInstructions(entity.getCookingInstructions())
                .build();
    }

}
