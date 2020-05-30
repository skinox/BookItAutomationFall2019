package com.bookit.units;

import com.bookit.utilities.APIUtilities;
import org.junit.Assert;
import org.junit.Test;

public class APIUtilitiesUnitTest {

    @Test
    public void getTokenTest(){
        String token = APIUtilities.getToken();
        String teacher_token = APIUtilities.getToken("teacher");
        String student_token = APIUtilities.getToken("student");
        Assert.assertNotNull(token);
        Assert.assertNotNull(student_token);
        Assert.assertNotNull(teacher_token);
    }
}
