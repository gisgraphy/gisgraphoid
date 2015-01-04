package com.gisgraphy.gisgraphoid;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;


public class JTSHelperTest {
    
    @Test
    public void checkLat(){
	try {
	    JTSHelper.checkLatitude(-1000D);
	    Assert.fail("too low latitude should throws");
	} catch (IllegalArgumentException e) {
	    //ignore
	}
	try {
	    JTSHelper.checkLatitude(1000D);
	    Assert.fail("too high latitude should throws");
	} catch (IllegalArgumentException e) {
	    //ignore
	}
	JTSHelper.checkLatitude(1D);
    }
    
    @Test
    public void checkLong(){
	try {
	    JTSHelper.checkLongitude(-1000D);
	    Assert.fail("too low latitude should throws");
	} catch (IllegalArgumentException e) {
	    //ignore
	}
	try {
	    JTSHelper.checkLongitude(1000D);
	    Assert.fail("too high longitude should throws");
	} catch (IllegalArgumentException e) {
	    //ignore
	}
	JTSHelper.checkLongitude(1D);
    }
    
    
    @Test
    public void createPointWithWrongLatitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    JTSHelper.createPoint(54.2F, -95F);
	    Assert.fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    JTSHelper.createPoint(54.2F, +95F);
	    Assert.fail("createPoint should only accept -90 < lattitude < 90");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }

    @Test
    public void createPointWithWrongLongitudeShouldThrowsAnIllegalArgumentException() {
	try {
	    JTSHelper.createPoint(-180.5F, 35F);
	    Assert.fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}

	try {
	    JTSHelper.createPoint(180.5F, +95F);
	    Assert.fail("createPoint should only accept -180 < longitude < 180");
	} catch (IllegalArgumentException e) {
	    // ok
	}
    }
    
    @Test
    public void parseInternationalDoubleShouldAcceptPointOrCommaAsDecimalSeparator() {
	try {
	    Assert.assertEquals(
		    "parseInternationalDouble should accept point as a decimal separator ",
		    3.2F, JTSHelper.parseInternationalDouble("3.2"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept comma as a decimal separator ",
		    3.2F, JTSHelper.parseInternationalDouble("3,2"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value without comma or point ",
		    3.0F, JTSHelper.parseInternationalDouble("3"),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with point ",
		    3.0F, JTSHelper.parseInternationalDouble("3."),0.1);
	    Assert.assertEquals(
		    "parseInternationalDouble should accept numeric value that ends with comma ",
		    3.0F, JTSHelper.parseInternationalDouble("3,"),0.1);
	} catch (ParseException e) {
	    Assert.fail();
	}
    }


}
