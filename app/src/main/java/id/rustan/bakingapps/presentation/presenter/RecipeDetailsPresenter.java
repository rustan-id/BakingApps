package id.rustan.bakingapps.presentation.presenter;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.interactor.RecipeDetailsInteractor;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.view.IRecipeDetailsView;



@InjectViewState
public class RecipeDetailsPresenter extends MvpPresenter<IRecipeDetailsView> {

    @Inject Router router;
    @Inject RecipeDetailsInteractor recipeDetailsInteractor;

    private Disposable disposable;
    private long id;

    public RecipeDetailsPresenter(AppComponent component){
        component.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Bundle args = router.getArguments(getClass().getName());
        id = args.getLong(KeyArguments.ID);
    }

    @Override
    public void attachView(IRecipeDetailsView view) {
        super.attachView(view);
        disposable = recipeDetailsInteractor.getRecipe(id)
                .subscribe(recipe -> getViewState().showDetails(recipe));
    }

    @Override
    public void detachView(IRecipeDetailsView view) {
        super.detachView(view);
        disposable.dispose();
    }

    public void onItemClick(int position){
        Bundle args = new Bundle();
        args.putLong(KeyArguments.ID, id);
        args.putInt(KeyArguments.STEP, position);
        Command command = position == 0? Command.SHOW_INGREDIENTS : Command.SHOW_STEP;
        router.putCommand(command, IngredientAndStepsPresenter.class.getName(), args);
    }

}
