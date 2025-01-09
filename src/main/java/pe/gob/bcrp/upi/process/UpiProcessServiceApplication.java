package pe.gob.bcrp.upi.process;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UpiProcessServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(UpiProcessServiceApplication.class, args);

		CamelSpringBootApplicationController applicationController = ctx.getBean(CamelSpringBootApplicationController.class);
		applicationController.run();
	}

}
