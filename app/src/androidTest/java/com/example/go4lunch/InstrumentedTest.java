package com.example.go4lunch;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.Utils.PlaceStream;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.go4lunch", appContext.getPackageName());
    }

    @Test
    public void placeSearch() throws Exception{

        Observable<NearbySearch> obsNearbySearch = PlaceStream.streamNearbySearch("45.515731,-1.126769");
        TestObserver<NearbySearch> testObserver = new TestObserver<>();

        obsNearbySearch.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        NearbySearch test = testObserver.values().get(0);

        assertThat("test status",test.getStatus().equals("OK"));
        //assertEquals("OK", test.getStatus());

    }

    @Test
    public void placeDetail() throws Exception{


        Observable<Details> obsNearbySearch = PlaceStream.streamDetails("ChIJq76yMXqEAUgRsRjSZGxlE2g");
        TestObserver<Details> testObserver = new TestObserver<>();

        obsNearbySearch.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        Details test =testObserver.values().get(0);

        assertThat("status is ok",test.getStatus().equals("OK"));

        assertThat("name restaurant",test.getResult().getName().equals("l'Auberg'in"));


    }
}
