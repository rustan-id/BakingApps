package id.rustan.bakingapps.helper;



public interface IVPlayer {

    VPlayerState getState();

    void restoreState(VPlayerState state);

    void release();
}
