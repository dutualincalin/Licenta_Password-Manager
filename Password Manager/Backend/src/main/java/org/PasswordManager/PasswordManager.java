package org.PasswordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PasswordManager
{
    private static ApplicationContext applicationContext;

    public static void main( String[] args ) {
        applicationContext = SpringApplication.run(PasswordManager.class, args);
    }

    public static void shutdown(){
        SpringApplication.exit(applicationContext);
    }
}
