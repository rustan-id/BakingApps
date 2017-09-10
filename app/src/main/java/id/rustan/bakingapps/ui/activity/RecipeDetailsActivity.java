package id.rustan.bakingapps.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import javax.inject.Inject;

import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.INavigator;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.model.ActivityUtility;
import id.rustan.bakingapps.presentation.presenter.IngredientAndStepsPresenter;
import id.rustan.bakingapps.presentation.presenter.RecipeDetailsPresenter;
import id.rustan.bakingapps.ui.CustomActivity;
import id.rustan.bakingapps.ui.fragment.IngredientsFragment;
import id.rustan.bakingapps.ui.fragment.RecipeDetailsFragment;
import id.rustan.bakingapps.ui.fragment.StepFragment;

public class RecipeDetailsActivity extends CustomActivity implements INavigator {

    @Inject Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ((BakingApps)getApplication()).getAppComponent().inject(this);
        ActivityUtility.setDisplayHomeAsUpEnabled(this);
        showRecipeDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        router.attachToNavigator(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleCommand(Command command) {
        switch(command) {

            case SHOW_INGREDIENTS:{
                showIngredients();
                break;
            }

            case SHOW_STEP:{
                showStep();
                break;
            }

            default:
        }
    }



    private void showRecipeDetails(){
        if (isTablet){
            Bundle args = router.getArguments(RecipeDetailsPresenter.class.getName());
            long recipeId = args.getLong(KeyArguments.ID);
            addFragment(R.id.detail_container, () -> IngredientsFragment.newInstance(recipeId), false);
        }

        addFragment(R.id.master_container, RecipeDetailsFragment::new, false);
    }

    private void showIngredients(){
        if (isTablet){
            Bundle args = router.getArguments(IngredientAndStepsPresenter.class.getName());
            long recipeId = args.getLong(KeyArguments.ID);
            replaceFragment(R.id.detail_container, IngredientsFragment.newInstance(recipeId), false);
        }
        else{
            Intent intent = new Intent(this, IngredientAndStepsActivity.class);
            startActivity(intent);
        }
    }

    private void showStep(){
        if (isTablet){
            Bundle args = router.getArguments(IngredientAndStepsPresenter.class.getName());
            long recipeId = args.getLong(KeyArguments.ID);
            int position = args.getInt(KeyArguments.STEP);
            replaceFragment(R.id.detail_container, StepFragment.newInstance(recipeId, position - 1), false);
        }
        else{
            Intent intent = new Intent(this, IngredientAndStepsActivity.class);
            startActivity(intent);
        }
    }

    private void replaceFragment(@IdRes int containerId, Fragment f, boolean addToBackStack){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerId, f);
        if (addToBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }



}
