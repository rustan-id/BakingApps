package id.rustan.bakingapps.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.INavigator;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.ui.CustomActivity;

public class RecipeListActivity extends CustomActivity implements INavigator {

    @Inject Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ((BakingApps) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        router.attachToNavigator(this);
    }

    @Override
    public void handleCommand(Command command) {
        switch (command) {
            case SHOW_DETAILS: {
                showDetails();
                break;
            }

            default:
        }
    }

    private void showDetails() {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        startActivity(intent);
    }

}
