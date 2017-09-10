package id.rustan.bakingapps.di;

import javax.inject.Singleton;

import dagger.Component;
import id.rustan.bakingapps.repository.IRepository;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.presenter.IngredientAndStepsPresenter;
import id.rustan.bakingapps.presentation.presenter.IngredientsPresenter;
import id.rustan.bakingapps.presentation.presenter.RecipeDetailsPresenter;
import id.rustan.bakingapps.presentation.presenter.RecipeListPresenter;
import id.rustan.bakingapps.presentation.presenter.StepPresenter;
import id.rustan.bakingapps.ui.activity.RecipeDetailsActivity;
import id.rustan.bakingapps.ui.activity.RecipeListActivity;
import id.rustan.bakingapps.ui.fragment.RecipeDetailsFragment;
import id.rustan.bakingapps.ui.fragment.RecipeListFragment;
import id.rustan.bakingapps.ui.fragment.StepFragment;
import id.rustan.bakingapps.widget.RecipeViewsFactory;
import id.rustan.bakingapps.widget.RecipeWidget;
import id.rustan.bakingapps.widget.ServiceRecipeWidget;


@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    void inject(RecipeListActivity obj);
    void inject(RecipeDetailsActivity obj);
    void inject(RecipeListFragment obj);
    void inject(RecipeListPresenter obj);
    void inject(RecipeDetailsFragment obj);
    void inject(RecipeDetailsPresenter obj);
    void inject(IngredientsPresenter obj);
    void inject(StepPresenter obj);
    void inject(StepFragment obj);
    void inject(IngredientAndStepsPresenter obj);
    void inject(ServiceRecipeWidget obj);
    void inject(RecipeViewsFactory obj);
    void inject(RecipeWidget obj);

    Router Router();
    IRepository Repo();
}
