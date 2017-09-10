package id.rustan.bakingapps.interactor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.di.IoScheduler;
import id.rustan.bakingapps.di.UiScheduler;
import id.rustan.bakingapps.model.data.Ingredient;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.data.Step;


public class RecipeDetailsInteractor {

    private IRepository iRepository;
    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    public RecipeDetailsInteractor(IRepository iRepository, @IoScheduler Scheduler ioScheduler,
                                   @UiScheduler Scheduler uiScheduler){
        this.iRepository = iRepository;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }

    public Observable<Recipe> getRecipe(long id){
        return iRepository.getRecipe(id)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    public Observable<Step> getStep(long recipeId, int stepNum){
        return iRepository.getStep(recipeId, stepNum)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    public Observable<List<Ingredient>> getIngredients(long recipeId){
        return iRepository.getIngredients(recipeId)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

}
