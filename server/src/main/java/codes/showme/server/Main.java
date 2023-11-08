package codes.showme.server;


import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.ioc.springimpl.SpringInstanceProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"codes.showme"})
public class Main {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Main.class);
        app.addInitializers(applicationContext ->
                {
                    InstanceFactory.setInstanceProvider(new SpringInstanceProvider(applicationContext));
                }

        );
        app.run(args);
    }

}