package mobile.lichle.com.trackme.view;

import android.Manifest;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowApplication;

import mobile.lichle.com.trackme.AssertUtils;
import mobile.lichle.com.trackme.R;

import static com.google.common.truth.Truth.assertThat;


/**
 * Created by lichvl.dp on 10/1/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity mMainActivity;
    private ShadowApplication mApplication;

    @Before
    public void setUp() {
        mMainActivity = new MainActivity();
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        ActivityController<MainActivity> create = activityController.create();
        Robolectric.getBackgroundThreadScheduler().pause();
        ActivityController<MainActivity> start = create.start();
        ActivityController<MainActivity> resume = start.resume();
        mMainActivity = resume.get();

        mockUpApplication(mMainActivity);
    }

    private void mockUpApplication(MainActivity activity) {
        mApplication = Shadows.shadowOf(activity.getApplication());
    }

    @Test
    public void shouldProcessNextStepWhenLocationPermissionIsGranted() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        mApplication.grantPermissions(permissions);
        FloatingActionButton button = mMainActivity.findViewById(R.id.fab);
        button.performClick();
        AssertUtils.assertLogged(Log.DEBUG, "MainActivity", "Location permission is granted", null);
    }

    @Test
    public void shouldAppTitleShowAtMainActivityScreen(){
        assertThat(mMainActivity.getTitle()).isNotNull();
        assertThat(mMainActivity.getTitle().toString()).isEqualTo(mMainActivity.getString(R.string.app_name));
    }

}
