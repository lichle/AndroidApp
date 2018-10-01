package mobile.lichle.com.trackme.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by lichvl.dp on 10/1/2018.
 */

public class FormatterUtilsTest {

    @Test
    public void testConvertTimeToString() {
        int time = 150; //seconds
        String value = FormatterUtils.convertTimeToString(time);
        String expectedValue = "00:02:30";
        Assert.assertEquals(value, expectedValue);
    }

    @Test
    public void testConvertDistanceToString() {
        float distance = 2.430937f;
        String value = FormatterUtils.convertDistanceToString(distance);
        String expectedValue = "2.43 km";
        Assert.assertEquals(value, expectedValue);
    }

    @Test
    public void testConvertVelocityToString() {
        float velocity = 27.836487324f;
        String value = FormatterUtils.converVelocityToString(velocity);
        String expectedValue = "27.84 km/h";
        Assert.assertEquals(value, expectedValue);
    }

}
