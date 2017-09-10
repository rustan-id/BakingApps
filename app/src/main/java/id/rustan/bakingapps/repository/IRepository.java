package id.rustan.bakingapps.repository;

import java.util.List;

import io.reactivex.Observable;
import id.rustan.bakingapps.model.data.Ingredient;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.data.Step;



public interface IRepository {

    Observable<Long> putRecipes(List<Recipe> recipeList);

    Observable<List<Recipe>> getRecipeNames();

    Observable<Recipe> getRecipe(long id);

    Observable<Step> getStep(long recipeId, int num);

    Observable<List<Ingredient>> getIngredients(long recipeId);

}
