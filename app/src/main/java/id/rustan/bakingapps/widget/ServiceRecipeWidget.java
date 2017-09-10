package id.rustan.bakingapps.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import id.rustan.bakingapps.BakingApps;

public class ServiceRecipeWidget extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        BakingApps app = (BakingApps) getApplicationContext();
        return new RecipeViewsFactory(getApplicationContext(), intent, app.getAppComponent());
    }

}
