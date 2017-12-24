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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import test.com.amazingsoftware.integration.sample.config.SecurityLayerConfigTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SecurityLayerConfigTest.class }, loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class CreateEncodedPasswordTest {

	@Autowired
	PasswordEncoder bCryptPasswordEncoder;

	/**
	 * This test print the encrypted password to write in the user.properties
	 * file according to the Bcrypt algorithm considered so far the most secure
	 * 
	 * In user.properties is present the username and password for the spring security settings for username and password.
	 */
	@Test
	public void getStringForPassword() {

		String password = "1Password";

		String encodedPassword = bCryptPasswordEncoder.encode(password);

		System.out.println(encodedPassword);

		return;
	}

}
