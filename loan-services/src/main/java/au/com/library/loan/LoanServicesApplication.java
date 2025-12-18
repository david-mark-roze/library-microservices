package au.com.library.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LoanServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanServicesApplication.class, args);
	}

}
