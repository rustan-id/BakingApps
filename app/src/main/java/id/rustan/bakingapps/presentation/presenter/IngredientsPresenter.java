package id.rustan.bakingapps.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.interactor.RecipeDetailsInteractor;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.view.IIngredientsView;


@InjectViewState
public class IngredientsPresenter extends MvpPresenter<IIngredientsView> {

    @Inject Router router;
    @Inject RecipeDetailsInteractor recipeDetailsInteractor;

    private long recipeId;

    public IngredientsPresenter(AppComponent component) {
        component.inject(this);
    }

    public void init(long recipeId){
        this.recipeId = recipeId;
    }

    @Override
    public void attachView(IIngredientsView view) {
        super.attachView(view);
        recipeDetailsInteractor.getIngredients(recipeId)
                .subscribe(ingredients -> getViewState().showIngredients(ingredients));
    }

}
