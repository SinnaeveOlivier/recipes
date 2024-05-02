package be.recipes.demo.web;

import be.recipes.demo.service.RecipeService;
import be.recipes.demo.service.model.request.CreateIngredientRequest;
import be.recipes.demo.service.model.request.CreateRecipeRequest;
import be.recipes.demo.service.model.request.UpdateRecipeRequest;
import be.recipes.demo.service.model.response.IngredientVO;
import be.recipes.demo.service.model.response.RecipeDetailVO;
import be.recipes.demo.service.model.response.RecipeOverviewVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class RecipeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetOverview() throws Exception {
        RecipeOverviewVO overview = RecipeOverviewVO.builder()
                .id(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Recipe description")
                .cookingInstructions("Instruction")
                .build();

        when(recipeService.getOverview()).thenReturn(Collections.singletonList(overview));

        mockMvc.perform(get("/api/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(overview.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(overview.getName()))
                .andExpect(jsonPath("$[0].description").value(overview.getDescription()))
                .andExpect(jsonPath("$[0].cookingInstructions").value(overview.getCookingInstructions()));

        verify(recipeService).getOverview();
    }

    @Test
    public void testGetDetail() throws Exception {
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        RecipeDetailVO detail = RecipeDetailVO.builder()
                .id(recipeId)
                .name("Test Recipe")
                .description("Test Recipe description")
                .cookingInstructions("Instruction")
                .ingredients(Lists.newArrayList(IngredientVO.builder()
                        .id(ingredientId)
                        .name("Ingredient")
                        .quantity("2 Lepels")
                        .build()))
                .build();

        when(recipeService.getDetail(recipeId)).thenReturn(detail);

        mockMvc.perform(get("/api/recipe/{id}", recipeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(recipeId.toString()))
                .andExpect(jsonPath("$.name").value(detail.getName()))
                .andExpect(jsonPath("$.description").value(detail.getDescription()))
                .andExpect(jsonPath("$.cookingInstructions").value(detail.getCookingInstructions()))
                .andExpect(jsonPath("$.ingredients[0].id").value(ingredientId.toString()))
                .andExpect(jsonPath("$.ingredients[0].name").value("Ingredient"))
                .andExpect(jsonPath("$.ingredients[0].quantity").value("2 Lepels"));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        CreateRecipeRequest request = new CreateRecipeRequest(
                "Test Recipe",
                "Test Recipe description",
                "Instruction",
                Lists.newArrayList(new CreateIngredientRequest(
                        "Ingredient",
                        "2 Lepels"))
        );
        when(recipeService.saveRecipe(request)).thenReturn(RecipeDetailVO.from(request.toDomain()));

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(recipeService).saveRecipe(request);
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();
        UpdateRecipeRequest request = new UpdateRecipeRequest(
                "Test Recipe",
                "Test Recipe description",
                "Instruction",
                Lists.newArrayList()
        );

        mockMvc.perform(put("/api/recipe/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(recipeService).updateRecipe(recipeId, request);
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();

        doNothing().when(recipeService).deleteRecipe(recipeId);

        mockMvc.perform(delete("/api/recipe/{id}", recipeId))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddIngredient() throws Exception {
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        CreateIngredientRequest request = new CreateIngredientRequest(
                "Ingredient Name",
                "2 lepels"
        );

        doNothing().when(recipeService).deleteIngredient(recipeId, ingredientId);

        mockMvc.perform(post("/api/recipe/{recipeId}/ingredient", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(recipeService).addIngredient(recipeId, request);
    }

    @Test
    public void testDeleteIngredient() throws Exception {
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();

        doNothing().when(recipeService).deleteIngredient(recipeId, ingredientId);

        mockMvc.perform(delete("/api/recipe/{recipeId}/ingredient/{id}", recipeId, ingredientId))
                .andExpect(status().isOk());
    }

}

