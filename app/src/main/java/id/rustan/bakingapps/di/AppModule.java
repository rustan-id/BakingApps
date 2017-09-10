package id.rustan.bakingapps.di;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import id.rustan.bakingapps.repository.RepositoryHelper;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import id.rustan.bakingapps.network.AutoValueGsonFactory;
import id.rustan.bakingapps.network.HttpClient;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.repository.Repository;
import id.rustan.bakingapps.helper.TimeController;
import id.rustan.bakingapps.repository.Converter;
import id.rustan.bakingapps.interactor.RecipeDetailsInteractor;
import id.rustan.bakingapps.interactor.RecipeInteractor;
import id.rustan.bakingapps.interactor.WidgetInteractor;
import id.rustan.bakingapps.model.Router;



@Module
public class AppModule {

    private final Context context;
    private static final String PREF_NAME = "app_preferences";

    public AppModule(Context context){
        this.context = context;
    }

    @ApplicationContext
    @Singleton
    @Provides
    public Context provideApplicationContext(){
        return context;
    }

    @Singleton
    @Provides
    public IRepository provideRepo(RepositoryHelper repositoryHelper){
        Converter converter = new Converter();
        return new Repository(repositoryHelper, converter);
    }

    @Singleton
    @Provides
    Router provideRouter(){
        return new Router();
    }

    @Singleton
    @Provides
    public HttpClient provideClient(){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create())
                        .create());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpClient.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();

        return retrofit.create(HttpClient.class);
    }

    @IoScheduler
    @Singleton
    @Provides
    public Scheduler provideIoScheduler(){
        return Schedulers.io();
    }

    @UiScheduler
    @Singleton
    @Provides
    public Scheduler provideUiScheduler(){
        return AndroidSchedulers.mainThread();
    }

    @Singleton
    @Provides
    public RecipeInteractor provideRecipeInteractor(IRepository repo, HttpClient client, TimeController timeController,
                                                    @IoScheduler Scheduler ioScheduler, @UiScheduler Scheduler uiScheduler){
        return new RecipeInteractor(repo, client, timeController, ioScheduler, uiScheduler);
    }

    @Singleton
    @Provides
    public RecipeDetailsInteractor provideRecipeDetailsInteractor(IRepository repo,
                            @IoScheduler Scheduler ioScheduler, @UiScheduler Scheduler uiScheduler){
        return new RecipeDetailsInteractor(repo, ioScheduler, uiScheduler);
    }

    @Singleton
    @Provides
    public SharedPreferences providePreferences(@ApplicationContext Context context){
        return context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    public TimeController provideTimeController(SharedPreferences pref){
        return new TimeController(pref);
    }

    @Singleton
    @Provides
    public WidgetInteractor provideWidgetInteractor(IRepository repo, SharedPreferences pref){
        return new WidgetInteractor(repo, pref);
    }

}
