package br.com.pegasus.api.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"br.com.pegasus.api.restful", // pacote principal do projeto
		"br.com.pegasus.gen" // pacote com os subpacotes do template
})
public class StartPegasusRestfulApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartPegasusRestfulApplication.class, args);
	}

}
