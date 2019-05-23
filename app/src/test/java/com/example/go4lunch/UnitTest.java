package com.example.go4lunch;

import com.example.go4lunch.Models.ListRestaurant;
import com.example.go4lunch.Views.PlaceViewHolder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {

    @Test
    public void ratingTest() {
        double rating = 3;
        Integer test = 2;
        assertEquals(test, ListRestaurant.rating(rating));

    }

@Test
    public void convertDateFrenchTest(){
        String date ="1300";
        String language= "français";

        assertEquals("13h00", PlaceViewHolder.convertDate(date,language));
}

    @Test
    public void convertDateEnglishTest(){
        String date ="1300";
        String language= "English";

        assertEquals("1.00pm", PlaceViewHolder.convertDate(date,language));
    }

}