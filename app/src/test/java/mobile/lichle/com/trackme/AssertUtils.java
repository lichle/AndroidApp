package mobile.lichle.com.trackme;

import com.google.common.collect.Iterables;

import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;

/**
 * Created by lichvl.dp on 10/1/2018.
 */

public class AssertUtils {

    public static void assertLogged(int type, String tag, String msg, Throwable throwable) {
        ShadowLog.LogItem lastLog = Iterables.getLast(ShadowLog.getLogs());
        assertEquals(type, lastLog.type);
        assertEquals(msg, lastLog.msg);
        assertEquals(tag, lastLog.tag);
        assertEquals(throwable, lastLog.throwable);
    }

}
