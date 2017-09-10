package id.rustan.bakingapps.presentation.presenter;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.interactor.RecipeDetailsInteractor;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.view.IIngredientAndStepsView;



@InjectViewState
public class IngredientAndStepsPresenter extends MvpPresenter<IIngredientAndStepsView> {

    @Inject Router router;
    @Inject RecipeDetailsInteractor recipeDetailsInteractor;

    private long recipeId;
    private int position;
    private int count;

    public IngredientAndStepsPresenter(AppComponent component){
        component.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Bundle args = router.getArguments(this.getClass().getName());
        if (args != null){
            recipeId = args.getLong(KeyArguments.ID);
            position = args.getInt(KeyArguments.STEP);
        }
    }

    @Override
    public void attachView(IIngredientAndStepsView view) {
        super.attachView(view);
        recipeDetailsInteractor.getRecipe(recipeId)
                .subscribe(
                        recipe -> {
                            count = recipe.steps().size();
                            getViewState().showData(recipeId, position);
                            enableNavigation(0, position, count);
                        },
                        throwable -> {},
                        () -> {}
                );
    }

    public void toPreviousPart(){
        if (position > 0){
            position--;
            getViewState().showData(recipeId, position);
        }

        enableNavigation(0, position, count);
    }

    public void toNextPart(){
        if (position < count) {
            position++;
            getViewState().showData(recipeId, position);
        }

        enableNavigation(0, position, count);
    }

    private void enableNavigation(int min, int position, int max){
        getViewState().enableNavigation(position > min, position < max);
    }

}
