package id.rustan.bakingapps;

import android.app.Application;

import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.di.AppModule;
import id.rustan.bakingapps.di.DaggerAppComponent;


public class BakingApps extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = createAppComponent();
    }

    protected AppComponent createAppComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

}
