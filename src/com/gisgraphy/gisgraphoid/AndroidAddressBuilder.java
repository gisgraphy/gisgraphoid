package com.gisgraphy.gisgraphoid;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;

import com.gisgraphy.domain.valueobject.CountriesStaticData;
import com.gisgraphy.domain.valueobject.StreetDistance;

/**
 *
 *@author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 */
public class AndroidAddressBuilder {
	
	protected static final String STREET_BASE_URL = "http://services.gisgraphy.com/public/displaystreet.html?gid=";
	private Locale locale ;
	
	public AndroidAddressBuilder(Locale locale) {
		this.locale = locale;
	}
	
	public AndroidAddressBuilder() {
		this.locale = Locale.getDefault();
	}

	/**
	 * @param gisgraphyAddresses the gisgraphy address list
	 * @return a list of android address or an empty list.
	 */
	public List<Address> transformGisgraphyAddressesToAndroidAddresses(List<com.gisgraphy.addressparser.Address> gisgraphyAddresses) {
		List<Address> androidAddresses = new ArrayList<Address>();
		if (gisgraphyAddresses != null) {
			for (com.gisgraphy.addressparser.Address address : gisgraphyAddresses) {
				Address androidAddress = transformGisgraphyAddressToAndroidAddress(address);
				if (androidAddress != null) {
					androidAddresses.add(androidAddress);
				}
			}
		}
		return androidAddresses;
	}

	/**
	 * @param gisgraphyAddress a gisgraphy address
	 * @return an Android Address
	 */
	public Address transformGisgraphyAddressToAndroidAddress(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		Address androidAddress = new Address(locale);
		String countryCode = locale.getCountry();
		androidAddress.setCountryCode(countryCode);
		androidAddress.setCountryName(CountriesStaticData.getCountryNameFromCountryCode(countryCode));
		androidAddress.setLatitude(gisgraphyAddress.getLat());
		androidAddress.setLongitude(gisgraphyAddress.getLng());
		if (gisgraphyAddress.getStreetName() != null) {
			androidAddress.setFeatureName(gisgraphyAddress.getStreetName());
			androidAddress.setAddressLine(0, ComputeStreetLine(gisgraphyAddress));
			androidAddress.setAddressLine(1, computeCityLine(gisgraphyAddress));
		} else if (gisgraphyAddress.getCity() != null) {
			androidAddress.setFeatureName(gisgraphyAddress.getCity());
			androidAddress.setAddressLine(0, computeCityLine(gisgraphyAddress));
		}
		androidAddress.setLocality(gisgraphyAddress.getCity());
		androidAddress.setAdminArea(gisgraphyAddress.getState());
		androidAddress.setPostalCode(gisgraphyAddress.getZipCode());
		androidAddress.setUrl(buildAddressUrlFromGisgraphyAddress(gisgraphyAddress));
		return androidAddress;
	}

	private String ComputeStreetLine(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		if (gisgraphyAddress.getStreetName() != null) {
			return gisgraphyAddress.getStreetName();
		} else {
			return null;
		}
	}

	private String computeCityLine(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		StringBuffer sb = new StringBuffer();
		if (gisgraphyAddress.getZipCode() != null) {
			sb.append(gisgraphyAddress.getZipCode()).append(" ");
		}
		if (gisgraphyAddress.getCity() != null) {
			sb.append(gisgraphyAddress.getCity()).append(" ");
		}
		if (gisgraphyAddress.getState() != null) {
			sb.append(gisgraphyAddress.getState()).append(" ");
		}
		if (gisgraphyAddress.getCountryCode() != null) {
		    String countryName = CountriesStaticData.getCountryNameFromCountryCode(gisgraphyAddress.getCountryCode());
		    if (countryName!=null)
			sb.append(countryName).append(" ");
		}
		String line1 = sb.toString();
		if (line1.trim().length() != 0) {
			return line1.trim();
		} else {
			return null;
		}
	}

	protected String buildAddressUrlFromGisgraphyAddress(com.gisgraphy.addressparser.Address gisgraphyAddress) {
		if (gisgraphyAddress != null && gisgraphyAddress.getId() != null) {
			return STREET_BASE_URL + gisgraphyAddress.getId();
		} else {
			return null;
		}
	}
	
	protected String buildAddressUrlFromStreetDistance(StreetDistance street) {
		if (street != null && street.getGid() != null) {
			return STREET_BASE_URL + street.getGid();
		} else {
			return null;
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<Address> transformStreetsToAndroidAddresses(List<StreetDistance> streets) {
	    List<Address> androidAddresses = new ArrayList<Address>();
		if (streets != null) {
			for (StreetDistance street : streets) {
				Address androidAddress = transformStreetToAndroidAddress(street);
				if (street != null) {
					androidAddresses.add(androidAddress);
				}
			}
		}
		return androidAddresses;
	}

	public Address transformStreetToAndroidAddress(StreetDistance street) {
	    Address androidAddress = new Address(locale);
	    androidAddress.setLatitude(street.getLat());
	    androidAddress.setLongitude(street.getLng());
	    String countryCode = street.getCountryCode();
	    androidAddress.setCountryName(CountriesStaticData.getCountryNameFromCountryCode(countryCode));
	    androidAddress.setCountryCode(countryCode);
	    androidAddress.setFeatureName(street.getName());
	    androidAddress.setLocality(street.getIsIn());
	    androidAddress.setUrl(buildAddressUrlFromStreetDistance(street));
	    androidAddress.setAddressLine(0,street.getName());
	    androidAddress.setAddressLine(1, buildAddressLine1(street));
	    return androidAddress;
	}
	
	private String buildAddressLine1(StreetDistance streetDistance){
	    if (streetDistance != null){
		StringBuffer sb = new StringBuffer();
		if (streetDistance.getIsIn()!=null){
		    sb.append(streetDistance.getIsIn()).append(" ");
		}
		if (streetDistance.getCountryCode() != null) {
		    String countryName = CountriesStaticData.getCountryNameFromCountryCode(streetDistance.getCountryCode());
		    if (countryName!=null)
			sb.append(countryName).append(" ");
		}
		return sb.toString();
	    }else {
		return null;
	    }
	}


}
