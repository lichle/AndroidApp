package mobile.lichle.com.trackme.view;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by lich on 9/24/18.
 */

public abstract class BaseActivity extends AppCompatActivity {


    public void showMessage(@NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.show();
    }

}
