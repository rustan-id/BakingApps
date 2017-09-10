package id.rustan.bakingapps.ui;

import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.di.DaggerAppComponent;



public class MockApplication extends BakingApps {

    @Override
    protected AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModuleTest(this))
                .build();
    }

}
