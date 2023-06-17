package org.PasswordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PasswordManager
{
    public static void main( String[] args ) {
        SpringApplication.run(PasswordManager.class, args);
    }
}
