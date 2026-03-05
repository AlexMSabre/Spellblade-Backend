package com.spellblade;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

//main method!! dont worry about this.

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SpellbladeBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpellbladeBackendApplication.class, args);    

  }
  
  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return (String[] args) -> {
    };
  }

}
