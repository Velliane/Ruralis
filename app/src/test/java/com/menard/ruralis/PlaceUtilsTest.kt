package com.menard.ruralis

import android.content.Context
import android.location.Location
import com.menard.ruralis.add_places.DayEnum
import com.menard.ruralis.utils.*
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month

class PlaceUtilsTest {

    @Mock
    private lateinit var context: Context

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun formatOpeningHoursToLocalLanguage(){
        val openingsMonday = "Monday:Closed"
        val openingsFriday = "Friday:9h-12 - 14h-18h"

        whenever(context.getString(DayEnum.MONDAY.res)).thenReturn("Lundi")
        whenever(context.getString(DayEnum.FRIDAY.res)).thenReturn("Vendredi")
        whenever(context.getString(R.string.closed)).thenReturn("Fermé")

        val openingsMondayFormat = changeOpeningHoursToLocaleLanguage(openingsMonday, context)
        assertEquals("Lundi : Fermé", openingsMondayFormat)
        val openingsFridayFormat = changeOpeningHoursToLocaleLanguage(openingsFriday, context)
        assertEquals("Vendredi : 9h-12 - 14h-18h", openingsFridayFormat)
    }

    @Test
    fun formatTypeFromGoogleMapsSearch(){
        whenever(context.getString(R.string.type_product_shop)).thenReturn("Magasin de producteurs")
        whenever(context.getString(R.string.type_alcool)).thenReturn("Vin et spiritueux")
        whenever(context.getString(R.string.type_food_general)).thenReturn("Alimentation")

        val listOfType1 = arrayListOf("liquor_store", "food")
        assertEquals("Vin et spiritueux", setTypeForPlacesFromGoogleMaps(listOfType1, context))
        val listOfType2 = arrayListOf("grocery_or_supermarket")
        assertEquals("Magasin de producteurs", setTypeForPlacesFromGoogleMaps(listOfType2, context))
        val listOfType3 = arrayListOf("food")
        assertEquals("Alimentation", setTypeForPlacesFromGoogleMaps(listOfType3, context))
    }

    @Test
    fun testParseLocalDateToString(){
        val date = LocalDateTime.of(2020, 12, 1, 12, 23, 36)
        assertEquals("01/12/2020", parseLocalDateTimeToString(date))
    }

    @Test
    fun transformListOfStringToStringFormatted(){
        val listOfOpenings = arrayListOf("Lundi : 9h-18h", "Mardi : 9h-18h", "Jeudi : Fermé")
        val string = transformListOfOpeningToString(listOfOpenings)
        assertEquals("Lundi : 9h-18h,Mardi : 9h-18h,Jeudi : Fermé", string)
    }

    @Test
    fun testCloseDistanceToUser(){
        val userLocation = Location("")
        userLocation.latitude = 46.6286761
        userLocation.longitude = 5.2374655
        val location = Location("")
        location.latitude = 46.629843
        location.longitude = 5.226084

        assertEquals( "881m", distanceToUser(location, userLocation))
    }

    @Test
    fun testFarDistanceToUser(){
        val userLocation = Location("")
        userLocation.latitude = 46.6286761
        userLocation.longitude = 5.2374655
        val location = Location("")
        location.latitude = 46.6301028
        location.longitude = 5.223020300000001

        assertEquals( "1,12km", distanceToUser(location, userLocation))
    }

}