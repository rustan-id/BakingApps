package id.rustan.bakingapps.ui;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnitRunner;


public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    @NonNull
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(cl, MockApplication.class.getName(), context);
    }

}
