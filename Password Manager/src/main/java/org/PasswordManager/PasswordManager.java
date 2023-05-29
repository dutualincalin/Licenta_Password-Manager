package org.PasswordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PasswordManager
{
    // TODO: Find a way to save the configuration at the end of the program
    public static void main( String[] args ) {
        SpringApplication.run(PasswordManager.class, args);
    }
}
