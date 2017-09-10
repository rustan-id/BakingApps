package id.rustan.bakingapps.interactor;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import id.rustan.bakingapps.network.HttpClient;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.helper.TimeController;
import id.rustan.bakingapps.di.IoScheduler;
import id.rustan.bakingapps.di.UiScheduler;
import id.rustan.bakingapps.model.data.Recipe;


public class RecipeInteractor {

    private IRepository iRepository;
    private HttpClient httpClient;
    private TimeController timeController;
    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    public RecipeInteractor(IRepository iRepository, HttpClient httpClient, TimeController timeController,
                            @IoScheduler Scheduler ioScheduler, @UiScheduler Scheduler uiScheduler){
        this.iRepository = iRepository;
        this.httpClient = httpClient;
        this.timeController = timeController;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
    }

    public Observable<Long> updateRecipes(){
        return timeController.isItTimeToUpdate()
                .filter(result -> result == true)
                .concatMap(result -> httpClient.getRecipes())
                .concatMap(recipes -> iRepository.putRecipes(recipes))
                .doOnNext(aLong -> timeController.saveTimeOfLastUpdate())
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

    public Observable<Long> forceUpdateRecipes(){
        return httpClient.getRecipes()
                .concatMap(recipes -> iRepository.putRecipes(recipes))
                .doOnNext(aLong -> timeController.saveTimeOfLastUpdate())
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);

    }

    public Observable<List<Recipe>> subscribeToRecipes(){
        return iRepository.getRecipeNames()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler);
    }

}
