package be.recipes.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "RECIPE")
public class RecipeEntity {

    @Id
    UUID id;

    @Column
    String name;

    @Column
    String description;

    @Column
    String cookingInstructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<IngredientEntity> ingredients;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime creationDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime lastUpdatedDate;


    public void update(String name, String description, String cookingInstructions) {
        this.name = name;
        this.description = description;
        this.cookingInstructions = cookingInstructions;
        this.lastUpdatedDate = LocalDateTime.now();
    }

    public void addIngredient(IngredientEntity ingredient) {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }

    public void updateOrCreateIngredient(IngredientEntity ingredient) {
        if (ingredient.getId() == null) {
            ingredient.setId(UUID.randomUUID());
            addIngredient(ingredient);
        } else if (ingredients.contains(ingredient)) {
            IngredientEntity entity = ingredients.get(ingredients.indexOf(ingredient));
            entity.update(ingredient.getName(), ingredient.getQuantity());
        } else {
            addIngredient(ingredient);
        }
    }

    public void removeIngredient(UUID id) {
        ingredients.removeIf(ingredientEntity -> ingredientEntity.getId().equals(id));
    }
}
