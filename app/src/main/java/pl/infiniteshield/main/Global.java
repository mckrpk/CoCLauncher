package pl.infiniteshield.main;

import android.app.Application;

public class Global extends Application {

    /**
     * Global context of the application. Can be used to obtain resources, write/read to file in convenient way etc. Should
     * not be used to display dialogs! Dialogs must be displayed on the current (visible) Activity.
     */
    public static Global app;

    public IntentSender intentSender;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        intentSender = new IntentSender();
    }
}
