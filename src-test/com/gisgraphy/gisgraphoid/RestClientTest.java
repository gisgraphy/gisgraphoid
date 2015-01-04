package com.gisgraphy.gisgraphoid;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gisgraphy.addressparser.AddressResultsDto;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

	@Test
	public void ConstructorShouldSetUrlAndAddEndingSlash() {
		String webServiceUrl = "http://www.perdu.com";
		RestClient resClient = new RestClient(webServiceUrl);
		Assert.assertEquals(webServiceUrl+"/", resClient.getWebServiceUrl());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ConstructorWithNullUrl() {
		 new RestClient(null);
	}
	
	@Test
	public void get(){
		String webServiceUrl = "http://gisgraphy.free.fr/test-data/";
		RestClient client = new RestClient(webServiceUrl);
		AddressResultsDto o = client.get("champs_elysees_paris", AddressResultsDto.class, new HashMap<String, String>());
		Assert.assertNotNull(o.getResult());
		Assert.assertEquals(10, o.getResult().size());
	}
	
	@Test(expected=RuntimeException.class)
	public void getBadUrl(){
		String webServiceUrl = "http://gisgraphy";
		RestClient client = new RestClient(webServiceUrl);
		client.get("champs_elysees_paris", AddressResultsDto.class, new HashMap<String, String>());
	}
	


}
