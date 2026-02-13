package au.com.library.loan;

import au.com.library.loan.config.LoanConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableConfigurationProperties(LoanConfiguration.class)
@EnableFeignClients
@SpringBootApplication
public class LoanServicesApplication {

	static void main(String[] args) {
		SpringApplication.run(LoanServicesApplication.class, args);
	}
}
