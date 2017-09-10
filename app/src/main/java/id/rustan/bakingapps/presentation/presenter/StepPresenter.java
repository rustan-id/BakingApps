package id.rustan.bakingapps.presentation.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.interactor.RecipeDetailsInteractor;
import id.rustan.bakingapps.model.Router;
import id.rustan.bakingapps.presentation.view.IStepView;



@InjectViewState
public class StepPresenter extends MvpPresenter<IStepView> {

    @Inject Router router;
    @Inject RecipeDetailsInteractor recipeDetailsInteractor;

    private final String TAG = getClass().getName();
    private long recipeId;
    private int stepNum;

    public StepPresenter(AppComponent component){
        component.inject(this);
    }

    public void init(long recipeId, int stepNum){
        this.recipeId = recipeId;
        this.stepNum = stepNum;
    }

    public void start(){
        recipeDetailsInteractor.getStep(recipeId, stepNum)
                .subscribe(
                        step -> getViewState().showStep(step)
                );
    }

    public void stop(){
        getViewState().releaseResources();
    }



}
