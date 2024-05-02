package be.recipes.demo.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "INGREDIENT")
public class IngredientEntity {

    @Id
    UUID id;

    @Column
    String name;

    @Column
    String quantity;

    @Setter
    @ManyToOne()
    @JoinColumn(name= "RECIPE_ID")
    RecipeEntity recipe;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime creationDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime lastUpdatedDate;

    public void update(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
        this.lastUpdatedDate = LocalDateTime.now();
    }

}
