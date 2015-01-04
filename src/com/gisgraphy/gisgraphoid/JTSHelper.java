package com.gisgraphy.gisgraphoid;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.gisgraphy.domain.valueobject.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class JTSHelper {
    
    public static Point createPoint(Float longitude, Float latitude) {
	if (longitude < -180 || longitude > 180) {
	    throw new IllegalArgumentException(
		    "Longitude should be between -180 and 180");
	}
	if (latitude < -90 || latitude > 90) {
	    throw new IllegalArgumentException(
		    "latitude should be between -90 and 90");
	}
	GeometryFactory factory = new GeometryFactory(new PrecisionModel(
		PrecisionModel.FLOATING), SRID.WGS84_SRID.getSRID());
	Point point = (Point) factory.createPoint(new Coordinate(longitude,
		latitude));
	return point;
    }
    

    /**
     * @param latitude
     *            the latitude to test
     * @return true if correct
     * @throw new {@link IllegalArgumentException} if not correct
     */
    public static  boolean checkLatitude(double latitude) {
	if (latitude < -90 || latitude > 90) {
	    throw new IllegalArgumentException("latitude is out of bound");
	}
	return true;
    }

    /**
     * @param longitude
     *            the latitude to test
     * @return true if correct
     * @throw new {@link IllegalArgumentException} if not correct
     */
    public static boolean checkLongitude(double longitude) {
	if (longitude < -180 || longitude > 180) {
	    throw new IllegalArgumentException("longitude is out of bound");
	}
	return true;
    }

    
    /**
     * parse a string and return the corresponding double value, it accepts
     * comma or point as decimal separator
     * 
     * @param number
     *                the number with a point or a comma as decimal separator
     * @return the float value corresponding to the parsed string
     * @throws ParseException
     *                 in case of errors
     */
    public static Float parseInternationalDouble(String number)
	    throws ParseException {
	NumberFormat nffrench = NumberFormat.getInstance(Locale.FRENCH);
	NumberFormat nfus = NumberFormat.getInstance(Locale.US);

	Number numberToReturn = number.indexOf(',') != -1 ? nffrench
		.parse(number) : nfus.parse(number);
	return numberToReturn.floatValue();
    }


}
