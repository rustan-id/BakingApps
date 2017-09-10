package id.rustan.bakingapps.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import io.reactivex.Observable;
import id.rustan.bakingapps.di.AppModule;
import id.rustan.bakingapps.helper.TimeController;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.network.HttpClient;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class AppModuleTest extends AppModule {

    public AppModuleTest(Context context) {
        super(context);
    }

    @Override
    public HttpClient provideClient(){
        HttpClient httpClient = mock(HttpClient.class);
        Observable<List<Recipe>> recipeList = Observable.just(new RecipesTest().getRecipes());
        when(httpClient.getRecipes()).thenReturn(recipeList);
        return httpClient;
    }

    @Override
    public TimeController provideTimeController(SharedPreferences pref){
        TimeController timeController = mock(TimeController.class);
        when(timeController.isItTimeToUpdate()).thenReturn(Observable.just(true));
        doNothing().when(timeController).saveTimeOfLastUpdate();
        return timeController;
    }

}
