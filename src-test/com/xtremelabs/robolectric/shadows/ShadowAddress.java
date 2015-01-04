package com.xtremelabs.robolectric.shadows;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;


/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(Address.class)
public class ShadowAddress {
    private List<String> addressLines = new ArrayList<String>();
    private String locality;
    private String postalCode;
    private String adminArea;
    private String countryCode;
    private double longitude;
    private double latitude;
    private String countryName;
    private String url;
    private Locale locale;
    private String featureName;
    
    public void __constructor__(Locale locale) {
	    this.locale = locale;
	  }

    @Implementation
    public double getLatitude() {
        return latitude;
    }

    @Implementation
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Implementation
    public double getLongitude() {
        return longitude;
    }

    @Implementation
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Implementation
    public void setAddressLine(int index, String line) {
       addressLines.add(index, line);
    }

    @Implementation
    public String getAddressLine(int index) {
        return addressLines.size()<=index? null:addressLines.get(index);
    }

    @Implementation
    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Implementation
    public String getLocality() {
        return locality;
    }

    @Implementation
    public String getAdminArea() {
        return adminArea;
    }

    @Implementation
    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    @Implementation
    public String getPostalCode() {
        return postalCode;
    }

    @Implementation
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Implementation
    public String getCountryCode() {
        return countryCode;
    }

    @Implementation
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Implementation
    protected String getCountryName() {
        return countryName;
    }

    @Implementation
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Implementation
    public String getUrl() {
        return url;
    }

    @Implementation
    public void setUrl(String url) {
        this.url = url;
    }

    @Implementation
    public Locale getLocale() {
        return locale;
    }

    @Implementation
    public String getFeatureName() {
        return featureName;
    }

    @Implementation
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}