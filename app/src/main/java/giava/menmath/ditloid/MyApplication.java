package giava.menmath.ditloid;

import android.app.Application;
import android.content.Context;

/**
 * This class get the application context
 *
 * @author MenMath.GiaVa
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}