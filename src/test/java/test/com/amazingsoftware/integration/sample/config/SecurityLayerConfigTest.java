package test.com.amazingsoftware.integration.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amazingsoftware.integration.samples.arch.restinvokation.impl.ExtendedArchRestServiceSupportImpl;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.IRestCountriesUrlBuilder;
import com.amazingsoftware.integration.samples.rest.builder.urlcurrency.impl.RestCountriesUrlBuilderImpl;
import com.amazingsoftware.integration.samples.rest.service.currency.ICurrencyService;
import com.amazingsoftware.integration.samples.rest.service.currency.impl.CurrencyServiceImpl;
import com.amazingsoftware.integration.samples.utils.HttpUtils;

/**
 * @author al.casula
 * 
 *         Configuration file for the Security layer. Framework used: Spring
 *         Security.
 *
 */
@Configuration
@Profile("test")
public class SecurityLayerConfigTest {

	/**
	 * @return the Bean for the encryption of the password for the user set with
	 *         Spring Security to access the rest country currency service.
	 */
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

}
