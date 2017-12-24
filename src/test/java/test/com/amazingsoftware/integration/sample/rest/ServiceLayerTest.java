/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.com.amazingsoftware.integration.sample.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Base64;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.amazingsoftware.integration.samples.consts.BaseServiceConst;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.rest.service.currency.domain.CountryInfoResponse;
import com.amazingsoftware.integration.samples.utils.NumberUtil;

import test.com.amazingsoftware.integration.sample.config.ApplicationConfigPropertyTest;
import test.com.amazingsoftware.integration.sample.config.ServiceLayerConfigTest;
import test.com.amazingsoftware.integration.sample.consts.TestConsts;

/**
 * 
 * This class contains all the tests for the service layer.
 * 
 * @author al.casula
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceLayerConfigTest.class,
		ApplicationConfigPropertyTest.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class ServiceLayerTest {

	@Autowired
	ICurrencyService currencyService;

	@Rule
	public ExpectedException exceptionExpected = ExpectedException.none();

	private static Log logger = LogFactory.getLog(ServiceLayerTest.class);

	/**
	 * Tests that the size is correct if it's called the service which returns
	 * all the elements from the source rest service.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSizeIsCorrectWithNoPagination() throws Exception {

		List<CountryInfoResponse> currenciesInfo = currencyService.getAllCurrenciesInfo();

		assertNotNull(currenciesInfo);
		assertEquals(NumberUtil.checkIntegerNumber(TestConsts.REST_COUNTRIES_RESPONSE_SIZE, null),
				new Integer(currenciesInfo.size()));
	}

	/**
	 * Tests all elements are returned if no paginations is set and third
	 * element is Albania as expected.
	 * 
	 * This test is Data Driven based on the http://restcountries.eu/rest/v2/all
	 * Rest Service. Adjust or @Ignore it if the Rest Service change the order
	 * in which Data are returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testElementContentIsCorrectWithNoPagination() throws Exception {

		List<CountryInfoResponse> currenciesInfo = currencyService.getAllCurrenciesInfo();

		assertNotNull(currenciesInfo);
		assertEquals(NumberUtil.checkIntegerNumber(TestConsts.REST_COUNTRIES_RESPONSE_SIZE, null),
				new Integer(currenciesInfo.size()));
		assertTrue("Albania".equals(currenciesInfo.get(2).getName()));
	}

	/**
	 * Tests that with pagination set, service-layer method returns a number of
	 * elements equal to the page size and exactly the correct elements as
	 * expected.
	 * 
	 * This test is Data Driven based on the http://restcountries.eu/rest/v2/all
	 * Rest Service. Adjust or @Ignore it if the Rest Service change the order
	 * in which Data are returned.
	 * 
	 * Andorra is the sixth Element in the tested Rest Call.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getElementIsCorrectWithPagination() throws Exception {

		String pageNumber = "2";
		String pageSize = "5";

		List<CountryInfoResponse> currenciesInfo = currencyService.getCurrenciesInfo(new Integer(pageNumber),
				new Integer(pageSize));

		assertNotNull(currenciesInfo);
		assertEquals(NumberUtil.checkIntegerNumber(pageSize, null), new Integer(currenciesInfo.size()));
		assertTrue("Andorra".equals(currenciesInfo.get(0).getName()));
	}

	/**
	 * Tests that the number of elements are correct if pagination is set.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getSizeIsCorrectIfPaginationIsSet() throws Exception {

		String pageNumber = "2";
		String pageSize = "5";

		List<CountryInfoResponse> currenciesInfo = currencyService.getCurrenciesInfo(new Integer(pageNumber),
				new Integer(pageSize));

		assertNotNull(currenciesInfo);
		assertEquals(NumberUtil.checkIntegerNumber(pageSize, null), new Integer(currenciesInfo.size()));
	}

	/**
	 * Tests that if pagination requests elements that are not present, no elements are provided.
	 * In this case elements requested are from 490 to 500 and an empty list is returned.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListIsEmptyIfPaginationRequestValuesNotPresent() throws Exception {

		String pageNumber = "50";
		String pageSize = "10";

		List<CountryInfoResponse> currenciesInfo = currencyService.getCurrenciesInfo(new Integer(pageNumber),
				new Integer(pageSize));

		assertNotNull(currenciesInfo);
		assertEquals(new Integer("0"), new Integer(currenciesInfo.size()));
	}

	/**
	 * tests that if pagination request elements only partially present, only that elements are returned.
	 * 
	 * In this case, size of country and currencies is 250 elements. 
	 * Requested elements are from 240 to 250. Only the last ten elements are expected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testListContainsExactlyRemainingElementsIfLastElementRequestedIsGreaterThanCountrySize()
			throws Exception {

		/* Initial size is 250, requested elements are the last 10 */
		String pageNumber = "7";
		String pageSize = "40";

		List<CountryInfoResponse> currenciesInfo = currencyService.getCurrenciesInfo(new Integer(pageNumber),
				new Integer(pageSize));

		assertNotNull(currenciesInfo);
		assertEquals(new Integer("10"), new Integer(currenciesInfo.size()));
		assertEquals("Uruguay", currenciesInfo.get(0).getName());
		assertEquals("Zimbabwe", currenciesInfo.get(9).getName());
	}

	/**
	 * IllegalArgumentException should be returned if pageNumber is not provided.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIllegalArgumentExceptionIfPageNumberIsNotProvided() throws Exception {

		exceptionExpected.expect(IllegalArgumentException.class);
		exceptionExpected.expectMessage(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_PROVIDED);

		currencyService.getCurrenciesInfo(null, new Integer("5"));

	}

	/**
	 * IllegalArgumentException should be returned if pageSize is not provided.
	 * 
	 * @throws Exception
	 */

	@Test
	public void testIllegalArgumentExceptionIfPageSizeIsNotProvided() throws Exception {

		exceptionExpected.expect(IllegalArgumentException.class);
		exceptionExpected.expectMessage(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_PROVIDED);

		currencyService.getCurrenciesInfo(new Integer("4"), null);
	}

	/**
	 * IllegalArgumentException should be returned if pageNumber is negative.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIllegalArgumentExceptionIfPageNumberIsNegative() throws Exception {

		exceptionExpected.expect(IllegalArgumentException.class);
		exceptionExpected.expectMessage(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_VALID);

		currencyService.getCurrenciesInfo(new Integer("-4"), new Integer("4"));
	}

	/**
	 * IllegalArgumentException should be returned if pageSize is zero.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIllegalArgumentExceptionIfPageSizeIsZero() throws Exception {

		exceptionExpected.expect(IllegalArgumentException.class);
		exceptionExpected.expectMessage(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_VALID);

		currencyService.getCurrenciesInfo(new Integer("-4"), new Integer("0"));
	}

	/**
	 * IllegalArgumentException should be returned if pageSize is negative.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIllegalArgumentExceptionIfPageSizeIsNegative() throws Exception {

		exceptionExpected.expect(IllegalArgumentException.class);
		exceptionExpected.expectMessage(BaseServiceConst.ErrorMessages.PAGE_SIZE_OR_MESSAGE_NOT_VALID);

		currencyService.getCurrenciesInfo(new Integer("5"), new Integer("-4"));
	}


}
