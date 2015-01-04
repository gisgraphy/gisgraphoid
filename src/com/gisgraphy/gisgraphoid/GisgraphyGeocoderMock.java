package com.gisgraphy.gisgraphoid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;

/**
 * a simple mock that returns some addresses
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 * 
 */
public class GisgraphyGeocoderMock extends GisgraphyGeocoder {

    static boolean isPresent() {
	return true;
    }

    public void setBaseUrl(String url) {

    }


    public GisgraphyGeocoderMock(Context context, Locale locale, String url) {
	super(context, locale, url);
    }

    public GisgraphyGeocoderMock(Context context, Locale locale) {
	super(context, locale);
    }
    public GisgraphyGeocoderMock(Context context, String url) {
	super(context, url);
    }

    public GisgraphyGeocoderMock(Context context) {
	super(context);
    }

    /**
     * @return Paris and New york
     */
    @Override
    public List<Address> getFromLocation(double latitude, double longitude, int maxResults) throws IOException {
	return createParisAndNewYorkAddress();
    }

    /**
     * @return Paris and New york
     */
    @Override
    public List<Address> getFromLocationName(String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) throws IOException {
	return createParisAndNewYorkAddress();
    }

    /**
     * @return Paris and New york
     */
    @Override
    public List<Address> getFromLocationName(String locationName, int maxResults) throws IOException {
	return createParisAndNewYorkAddress();
    }

    private List<Address> createParisAndNewYorkAddress() {
	List<Address> androidAddress = new ArrayList<Address>();
	Address address1 = new Address(Locale.getDefault());
	address1.setLatitude(48.85340881347656D);
	address1.setLongitude(2.34879994392395D);
	address1.setFeatureName("Notre dame");
	address1.setLocality("Paris");
	Address address2 = new Address(Locale.getDefault());
	address2.setFeatureName("Broadway");
	address2.setLatitude(40.714271545410156D);
	address2.setLongitude(-74.00596618652344D);
	address2.setLocality("New york");
	androidAddress.add(address1);
	androidAddress.add(address2);
	return androidAddress;
    }

    /**
     * @return the apikey. apikey is only used for Gisgraphy premium services.
     *         It is not required for free services (when those lines are
     *         written)
     * @see http://www.gisgraphy.com/premium
     */
    public Long getApiKey() {
	return 123L;
    }

    /**
     * @param apiKey
     *            the apikey provided by gisgraphy apikey is used for Gisgraphy
     *            premium services. It is not required for free services (when
     *            those lines are written)
     * @see http://www.gisgraphy.com/premium
     */
    public void setApiKey(Long apiKey) {

    }

    public String getUrl() {
	return "http://mock.gisgraphy.com";
    }

    public void setUrl(String url) {
    }

    public Locale getLocale() {
	return Locale.getDefault();
    }

    public void setLocale(Locale locale) {
    }

}
