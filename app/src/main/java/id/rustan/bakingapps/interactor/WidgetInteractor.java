package id.rustan.bakingapps.interactor;

import android.content.SharedPreferences;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.model.data.Recipe;


public class WidgetInteractor {

    private final IRepository iRepository;
    private final SharedPreferences sharedPreferences;
    private int recipeIndex;

    private static final String RECIPE_INDEX_KEY = "recipe_index_key";

    public WidgetInteractor(IRepository iRepository, SharedPreferences sharedPreferences){
        this.iRepository = iRepository;
        this.sharedPreferences = sharedPreferences;
        recipeIndex = sharedPreferences.getInt(RECIPE_INDEX_KEY, 0);
    }

    public Observable<Boolean> incRecipeIndex(){
        recipeIndex = sharedPreferences.getInt(RECIPE_INDEX_KEY, 0);
        return iRepository.getRecipeNames()
                .map(List::size)
                .map(recipeAmount -> {
                    if (recipeIndex < recipeAmount - 1){
                        recipeIndex++;
                        sharedPreferences.edit().putInt(RECIPE_INDEX_KEY, recipeIndex).commit();
                        return true;
                    }
                    return false;
                });
    }

    public Observable<Boolean> decRecipeIndex(){
        boolean result = false;

        recipeIndex = sharedPreferences.getInt(RECIPE_INDEX_KEY, 0);
        if (recipeIndex > 0) {
            recipeIndex--;
            result = true;
        }

        sharedPreferences.edit().putInt(RECIPE_INDEX_KEY, recipeIndex).commit();
        return Observable.just(result);
    }

    public Observable<Recipe> getRecipe(){
        return iRepository.getRecipeNames()
                .map(recipes -> recipes.get(recipeIndex))
                .map(Recipe::id)
                .concatMap(iRepository::getRecipe)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline());
    }

}
