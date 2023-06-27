package ar.edu.utn.frc.tup.lciii;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
@SpringBootApplication
public class Ajedrez2Application {

    public static void main(String[] args) {
        SpringApplication.run(Ajedrez2Application.class, args);



    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Trabajo Práctico Integrador Ajedrez")
                .version("v.1")
                .description("Integrantes: Alex Abrahamian, Maximiliano Calvo, Matias Lokman, y Santiago Zaurrini"));
    }

}
