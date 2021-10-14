package uk.co.tuffdev.filestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uk.co.tuffdev.filestore.auth.config.AppProperties;
import uk.co.tuffdev.filestore.config.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, StorageProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
