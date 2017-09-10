package id.rustan.bakingapps.presentation.presenter;

import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.interactor.RecipeInteractor;
import id.rustan.bakingapps.model.KeyArguments;
import id.rustan.bakingapps.model.data.Recipe;
import id.rustan.bakingapps.model.Command;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.view.IRecipeView;


@InjectViewState
public class RecipeListPresenter extends MvpPresenter<IRecipeView> {

    @Inject Router router;
    @Inject RecipeInteractor recipeInteractor;

    private Disposable disposable;
    private List<Recipe> recipeList;
    private final String TAG = getClass().getName();

    public RecipeListPresenter(AppComponent component){
        component.inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        recipeInteractor.updateRecipes()
                .doOnSubscribe(result -> getViewState().showLoading())
                .doAfterTerminate(() -> getViewState().hideLoading())
                .subscribe(aLong -> {}, throwable -> Log.d(TAG, throwable.toString()), () -> {});
    }

    @Override
    public void attachView(IRecipeView view) {
        super.attachView(view);
        disposable = recipeInteractor.subscribeToRecipes()
                .subscribe(result -> {
                    this.recipeList = result;
                    getViewState().showRecipes(result);
                });
    }

    @Override
    public void detachView(IRecipeView view) {
        super.detachView(view);
        disposable.dispose();
    }

    public void showDetails(int position){
        Bundle args = new Bundle();
        args.putLong(KeyArguments.ID, recipeList.get(position).id());
        router.putCommand(Command.SHOW_DETAILS, RecipeDetailsPresenter.class.getName(), args);
    }

    public void forceUpdateRecipes(){
        recipeInteractor.forceUpdateRecipes()
                .doAfterTerminate(() -> getViewState().hideSwipeRefresh())
                .subscribe(
                        aLong -> {},
                        this::handleErrors,
                        () -> {}
                );
    }

    /** util methods */

    private void handleErrors(Throwable throwable){
        getViewState().showNetworkError();
    }

}
