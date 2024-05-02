package be.recipes.demo.repository;

import be.recipes.demo.repository.entity.RecipeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, UUID> {

}
