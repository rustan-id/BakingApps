package id.rustan.bakingapps.helper;

import android.content.SharedPreferences;

import io.reactivex.Observable;


public class TimeController {

    private static final String TAG = TimeController.class.getName();
    private static final String UPDATE_DATE = "update_date";
    private static final long UPDATE_INTERVAL_S = 10*60;
    private SharedPreferences sharedPreferences;

    public TimeController(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public Observable<Boolean> isItTimeToUpdate(){
        return Observable.just(sharedPreferences.getLong(UPDATE_DATE, 0))
                .map(date -> getCurDate() - date > UPDATE_INTERVAL_S);
    }

    public void saveTimeOfLastUpdate(){
        sharedPreferences.edit()
                .putLong(UPDATE_DATE, getCurDate())
                .apply();
    }

    private long getCurDate() {
        long curTime = System.currentTimeMillis();
        return curTime == 0 ? 0:curTime/1000;
    }

}
