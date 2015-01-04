package com.gisgraphy.gisgraphoid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.location.Address;

import com.gisgraphy.addressparser.AddressResultsDto;
import com.gisgraphy.domain.valueobject.StreetDistance;
import com.gisgraphy.domain.valueobject.StreetDistance.StreetDistanceBuilder;
import com.gisgraphy.domain.valueobject.StreetSearchResultsDto;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
@RunWith(RobolectricTestRunner.class)
public class GisgraphyGeocoderTest {
    
    @Test
    public void isPresent(){
    	Assert.assertTrue(GisgraphyGeocoder.isPresent());
    }
    /*
     *                   _                   _             
      ___ ___  _ __  ___| |_ _ __ _   _  ___| |_ ___  _ __ 
     / __/ _ \| '_ \/ __| __| '__| | | |/ __| __/ _ \| '__|
    | (_| (_) | | | \__ \ |_| |  | |_| | (__| || (_) | |   
     \___\___/|_| |_|___/\__|_|   \__,_|\___|\__\___/|_|   

     */

    @Test(expected=IllegalArgumentException.class)
    public void setBaseUrlWithNullUrlShouldThrowsIllegalArgumentException() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	geocoder.setBaseUrl(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void setBaseUrlWithMalformedUrlUrlShouldThrowsIllegalArgumentException() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	geocoder.setBaseUrl("htp://foo");
    }
    
    
    @Test
    public void setBaseUrlShouldSetTheUrl() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	String changedUrl="http://foo.com";
	geocoder.setBaseUrl(changedUrl);
	Assert.assertEquals(changedUrl,geocoder.getBaseUrl());
    }
    
    @Test
    public void defaultBaseUrl() {
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	Assert.assertEquals(GisgraphyGeocoder.DEFAULT_BASE_URL,geocoder.getBaseUrl());
    }
    
    @Test
    public void ConstructorShouldSetTheLocale(){
	Locale locale =Locale.CANADA;
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null,locale);
	Assert.assertNotNull(geocoder.addressBuilder);
	Assert.assertEquals(locale, geocoder.addressBuilder.getLocale());
	Assert.assertEquals(locale, geocoder.getLocale());
    }
    
    @Test
    public void ConstructorShouldSetTheLocaleAndUrl(){
	Locale locale =Locale.CANADA;
	String url = "http://www.gisgraphy.com/";
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null,locale,url);
	Assert.assertNotNull(geocoder.addressBuilder);
	Assert.assertEquals(locale, geocoder.addressBuilder.getLocale());
	Assert.assertEquals(locale, geocoder.getLocale());
	Assert.assertEquals(url, geocoder.getBaseUrl());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptMalformedUrl(){
	Locale locale =Locale.CANADA;
	String url = "htp://foo";
	new GisgraphyGeocoder(null,locale,url);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptNullLocale(){
	String url = "http://foo";
	new GisgraphyGeocoder(null,null,url);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void ConstructorShouldNotAcceptNullLocale_bis(){
	Locale locale =null;
	new GisgraphyGeocoder(null,locale);
    }
    
    @Test
    public void ConstructorShouldSetTheDefaultLocale(){
	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
	Assert.assertEquals(Locale.getDefault(),geocoder.getLocale());
    }
    
    /*
                                              _       
     _ __   __ _ _ __ ___   ___    ___  _ __ | |_   _ 
    | '_ \ / _` | '_ ` _ \ / _ \  / _ \| '_ \| | | | |
    | | | | (_| | | | | | |  __/ | (_) | | | | | |_| |
    |_| |_|\__,_|_| |_| |_|\___|  \___/|_| |_|_|\__, |
                                                |___/ 

     */
    @Test(expected=IOException.class)
    public void getFromLocationName_Exception() throws IOException{
    	String addressToGeocode = "address to geocode";
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.ADDRESS_PARAMETER_NAME	, addressToGeocode);
    	params.put(GisgraphyGeocoder.COUNTRY_PARAMETER_NAME	, Locale.getDefault().getCountry());
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.GEOCODING_URI, AddressResultsDto.class, params)).andStubThrow(new RuntimeException());
    	EasyMock.replay(restClientMock);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	geocoder.getFromLocationName(addressToGeocode, numberOfResults);
    }
    
    @Test
    public void getFromLocationName() throws IOException{
    	String addressToGeocode = "address to geocode";
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	List<com.gisgraphy.addressparser.Address> gisgraphyAddress = createGisgraphyAddresses(10);
    	AddressResultsDto addressResultsDto = new AddressResultsDto(gisgraphyAddress,10L);
    	
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	//EasyMock.expect(restClientMock.getWebServiceUrl()).andReturn(baseUrl);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.ADDRESS_PARAMETER_NAME	, addressToGeocode);
    	params.put(GisgraphyGeocoder.COUNTRY_PARAMETER_NAME	, Locale.getDefault().getCountry());
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.GEOCODING_URI, AddressResultsDto.class, params)).andReturn(addressResultsDto);
    	EasyMock.replay(restClientMock);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	List<Address> AndroidAddress = geocoder.getFromLocationName(addressToGeocode, numberOfResults);
    	EasyMock.verify(restClientMock);
    	Assert.assertEquals("the max parameter should be taken into account",numberOfResults, AndroidAddress.size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameWithNullLocationName() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName(null, 6);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameWithNegativeMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", -1);
    }
    
    @Test
    public void getFromLocationNameWithZeroMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	List<Address> addresses = geocoder.getFromLocationName("test", 0);
    	Assert.assertNotNull(addresses);
    	Assert.assertEquals(0, addresses.size());
    }
    
    
    /*
     *                                            _ 
     _ __   __ _ _ __ ___   ___    __ _ _ __   __| |
    | '_ \ / _` | '_ ` _ \ / _ \  / _` | '_ \ / _` |
    | | | | (_| | | | | | |  __/ | (_| | | | | (_| |
    |_| |_|\__,_|_| |_| |_|\___|  \__,_|_| |_|\__,_|
                                                    
     _                           _ _               _               
    | |__   ___  _   _ _ __   __| (_)_ __   __ _  | |__   _____  __
    | '_ \ / _ \| | | | '_ \ / _` | | '_ \ / _` | | '_ \ / _ \ \/ /
    | |_) | (_) | |_| | | | | (_| | | | | | (_| | | |_) | (_) >  < 
    |_.__/ \___/ \__,_|_| |_|\__,_|_|_| |_|\__, | |_.__/ \___/_/\_\

     */
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBoxWithNullLocationName() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName(null, 6,-45,45,-20,20);
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_loweleftlatTooHigh() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,1000,45,-20,20);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_loweleftlatTooSmall() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,-1000,45,-20,20);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_loweleftLongTooHigh() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,45,1000,-20,20);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_loweleftLongTooSmall() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,45,-1000,-20,20);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_upperRightlatTooHigh() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,-45,45,1000,20);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_upperRightLatTooSmall() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,-45,45,-1000,20);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_upperRightlongTooHigh() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,-45,45,45,1000);
    }
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBox_upperRightLongTooSmall() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", 10,-45,45,-45,-1000);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationNameAndBoundingBoxWithNegativeMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocationName("test", -1 ,10,-45,45,-45);
    }
    
    @Test
    public void getFromLocationNameAndBoundingBoxWithZeroMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	List<Address> addresses = geocoder.getFromLocationName("test", 0, 10,-45,45,-45);
    	Assert.assertNotNull(addresses);
    	Assert.assertEquals(0, addresses.size());
    }
    
    
    @Test
    public void getFromLocationNameAndBoundingBoxShouldTakeMaxParameterIntoAccount() throws IOException{
	String addressToGeocode = "address to geocode";
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	List<com.gisgraphy.addressparser.Address> gisgraphyAddress = createGisgraphyAddresses(10);
    	AddressResultsDto addressResultsDto = new AddressResultsDto(gisgraphyAddress,10L);
    	
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	//EasyMock.expect(restClientMock.getWebServiceUrl()).andReturn(baseUrl);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.ADDRESS_PARAMETER_NAME	, addressToGeocode);
    	params.put(GisgraphyGeocoder.COUNTRY_PARAMETER_NAME	, Locale.getDefault().getCountry());
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.GEOCODING_URI, AddressResultsDto.class, params)).andReturn(addressResultsDto);
    	EasyMock.replay(restClientMock);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	List<Address> addresses = geocoder.getFromLocationName(addressToGeocode, numberOfResults, -90,-180,90,180);
    	EasyMock.verify(restClientMock);
    	Assert.assertEquals("the max parameter should be taken into account",numberOfResults, addresses.size());
    }
    
    @Test
    public void getFromLocationNameAndBoundingBoxShouldFilter() throws IOException{
	String addressToGeocode = "address to geocode";
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	List<com.gisgraphy.addressparser.Address> gisgraphyAddress = createGisgraphyAddresses(2);
    	//set out of the bounding box
    	gisgraphyAddress.get(0).setLat(-10D);
    	gisgraphyAddress.get(0).setLng(-20D);
    	
    	gisgraphyAddress.get(1).setLat(51D);
    	gisgraphyAddress.get(1).setLng(21D);
    	
    	AddressResultsDto addressResultsDto = new AddressResultsDto(gisgraphyAddress,10L);
    	
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	//EasyMock.expect(restClientMock.getWebServiceUrl()).andReturn(baseUrl);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.ADDRESS_PARAMETER_NAME	, addressToGeocode);
    	params.put(GisgraphyGeocoder.COUNTRY_PARAMETER_NAME	, Locale.getDefault().getCountry());
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.GEOCODING_URI, AddressResultsDto.class, params)).andReturn(addressResultsDto);
    	EasyMock.replay(restClientMock);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	List<Address> addresses = geocoder.getFromLocationName(addressToGeocode, numberOfResults, 50,20,52,22);
    	EasyMock.verify(restClientMock);
    	Assert.assertEquals(1, addresses.size());
    	Assert.assertEquals(51D, addresses.get(0).getLatitude());
    	Assert.assertEquals(21D, addresses.get(0).getLongitude());
    }
    
    //utils
    private List<com.gisgraphy.addressparser.Address> createGisgraphyAddresses(int numberOfResults){
    	List<com.gisgraphy.addressparser.Address> results = new ArrayList<com.gisgraphy.addressparser.Address>();
    	for (int i = 0;i<numberOfResults;i++){
    		com.gisgraphy.addressparser.Address address = new com.gisgraphy.addressparser.Address();
    		address.setId(new Long(i));
    		address.setLat(new Double(i+20));
    		address.setLng(new Double((i+20)*-1));
    		address.setCity("city"+i);
    		address.setStreetName("street"+i);
    		results.add(address);
    	}
    	return results;
    }
    
    private List<StreetDistance> createStreetDistance(int numberOfResults){
    	List<StreetDistance> results = new ArrayList<StreetDistance>();
    	for (int i = 0;i<numberOfResults;i++){
    		StreetDistance streetDistance = StreetDistanceBuilder
    		.streetDistance()
    		.withName("name"+i)
    		.withCountryCode("FR")
    		.withDistance(new Double(i))
    		.withGid(new Long(i)).
    		withIsIn("city")
    		.withLocation(JTSHelper.createPoint(new Float(i+20),new Float(i+10)))
    		.withLength(new Double(i))
    		.build();
    		results.add(streetDistance);
    	}
    	return results;
    }
    
    /*
     * 
      __                       _                 _   _             
     / _|_ __ ___  _ __ ___   | | ___   ___ __ _| |_(_) ___  _ __  
    | |_| '__/ _ \| '_ ` _ \  | |/ _ \ / __/ _` | __| |/ _ \| '_ \ 
    |  _| | | (_) | | | | | | | | (_) | (_| (_| | |_| | (_) | | | |
    |_| |_|  \___/|_| |_| |_| |_|\___/ \___\__,_|\__|_|\___/|_| |_|

     */
    
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationWithNegativeMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocation(20D,30D,-1);
    }
    
    @Test
    public void getFromLocationWithZeroMaxResults() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	List<Address> addresses = geocoder.getFromLocation(20D,30D,0);
    	Assert.assertNotNull(addresses);
    	Assert.assertEquals(0, addresses.size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationWithToolowLat() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocation(-1000D,30D,0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationWithTooHighLat() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocation(1000D,30D,0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationWithTooHighong() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocation(30D,1000D,0);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void getFromLocationWithToolowLong() throws IOException{
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null);
    	geocoder.getFromLocation(30D,-1000D,0);
    }
    
    @Test
    public void getFromLocationShouldTakeMaxParameterIntoAccount() throws IOException{
    	Long apiKey=123L;
    	int numberOfResults = 6;
    	List<StreetDistance> streets = createStreetDistance(10);
    	StreetSearchResultsDto streetSearchResultsDto = new StreetSearchResultsDto(streets,10L,null);
    	double lat=12D;
    	double lng=25D;
    	final RestClient restClientMock = EasyMock.createMock(RestClient.class);
    	HashMap<String, String> params = new HashMap<String, String>();
    	params.put(GisgraphyGeocoder.FORMAT_PARAMETER_NAME	, GisgraphyGeocoder.DEFAULT_FORMAT);
    	params.put(GisgraphyGeocoder.LATITUDE_PARAMETER_NAME	, lat+"");
    	params.put(GisgraphyGeocoder.LONGITUDE_PARAMETER_NAME	, lng+"");
    	params.put(GisgraphyGeocoder.APIKEY_PARAMETER_NAME	, apiKey+"");
    	EasyMock.expect(restClientMock.get(GisgraphyGeocoder.REVERSE_GEOCODING_URI, StreetSearchResultsDto.class, params)).andReturn(streetSearchResultsDto);
    	EasyMock.replay(restClientMock);
    	GisgraphyGeocoder geocoder = new GisgraphyGeocoder(null){
    		@Override
    		protected RestClient createRestClient() {
    			return restClientMock;
    		}
    		@Override
    		protected void log_d(String message) {
    		}
    	};
    	geocoder.setApiKey(apiKey);
    	List<Address> addresses = geocoder.getFromLocation(lat,lng,numberOfResults);
    	EasyMock.verify(restClientMock);
    	Assert.assertEquals("the max parameter should be taken into account",numberOfResults, addresses.size());
    }
    

}
