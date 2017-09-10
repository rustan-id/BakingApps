package id.rustan.bakingapps.helper;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class VPlayerState implements Parcelable {

    public abstract long position();

    public abstract boolean isPlaying();

    public static VPlayerState create(long position, boolean isPlaying) {
        return new AutoValue_VPlayerState(position, isPlaying);
    }
}
