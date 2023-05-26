package br.weg.sod;

import br.weg.sod.model.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SodApplication {

	public static void main(String[] args) {
		SpringApplication.run(SodApplication.class, args);
	}

}
