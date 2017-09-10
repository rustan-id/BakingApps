package id.rustan.bakingapps.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rustan.bakingapps.BakingApps;
import id.rustan.bakingapps.R;
import id.rustan.bakingapps.di.AppComponent;
import id.rustan.bakingapps.helper.GlideApp;
import id.rustan.bakingapps.helper.ExoPlayer;
import id.rustan.bakingapps.helper.IVPlayer;
import id.rustan.bakingapps.helper.VPlayerState;
import id.rustan.bakingapps.model.data.Step;
import id.rustan.bakingapps.presentation.presenter.StepPresenter;
import id.rustan.bakingapps.presentation.view.IStepView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends MvpAppCompatFragment implements IStepView {

    @InjectPresenter
    StepPresenter presenter;

    @BindView(R.id.empty_view) TextView emptyView;
    @BindView(R.id.video_frame) FrameLayout videoFrameView;
    @BindView(R.id.player_view) SimpleExoPlayerView playerView;
    @BindView(R.id.step_description)  TextView stepDescriptionView;
    @BindView(R.id.thumbnail_url) ImageView thumbnailView;

    private IVPlayer videoPlayer;
    private VPlayerState vPlayerState;
    private final String TAG = getClass().getName();
    private static final String RECIPE_ID_KEY = "recipe_id";
    private static final String STEP_NUM_KEY = "step_num";
    private static final String VIDEO_PLAYER_STATE_KEY = "video_player_state";

    public static StepFragment newInstance(long recipeId, int step){
        Bundle args = new Bundle();
        args.putLong(RECIPE_ID_KEY, recipeId);
        args.putInt(STEP_NUM_KEY, step);
        StepFragment f = new StepFragment();
        f.setArguments(args);
        return f;
    }

    @ProvidePresenter
    StepPresenter providePresenter(){
        AppComponent component = ((BakingApps)getActivity().getApplication()).getAppComponent();
        return new StepPresenter(component);
    }

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && savedInstanceState == null){
            presenter.init(args.getLong(RECIPE_ID_KEY), args.getInt(STEP_NUM_KEY));
        }

        if (savedInstanceState != null){
            vPlayerState = savedInstanceState.getParcelable(VIDEO_PLAYER_STATE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //load and show data
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        vPlayerState = videoPlayer == null? null : videoPlayer.getState();
        //release resources
        presenter.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (vPlayerState != null) {
            outState.putParcelable(VIDEO_PLAYER_STATE_KEY, vPlayerState);
        }
    }

    @Override
    public void showStep(Step step) {
        String videoUrl = step.videoURL();
        if (videoUrl == null || videoUrl.isEmpty()) {
            playerView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            videoPlayer = ExoPlayer.getInstance(getContext(), playerView, step.videoURL());
            videoPlayer.restoreState(vPlayerState);
        }

        String thumbnailUrl = step.thumbnailURL();
        if (thumbnailUrl == null || thumbnailUrl.isEmpty()){
            thumbnailView.setVisibility(View.GONE);
        }
        else{
            thumbnailView.setVisibility(View.VISIBLE);
            GlideApp.with(getContext())
                    .load(thumbnailUrl)
                    .error(R.mipmap.ic_launcher)
                    .into(thumbnailView);
        }

        stepDescriptionView.setText(step.description());
    }

    @Override
    public void releaseResources(){
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }
    }

}
