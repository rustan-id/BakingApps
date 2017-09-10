package id.rustan.bakingapps.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import id.rustan.bakingapps.model.data.Step;



public interface IStepView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showStep(Step step);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void releaseResources();

}
