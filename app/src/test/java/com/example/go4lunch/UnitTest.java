package com.example.go4lunch;

import com.example.go4lunch.Models.ListRestaurant;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void ratingTest() {
        double rating = 3;
        Integer test = 2;
        assertEquals(test, ListRestaurant.rating(rating));

    }


}