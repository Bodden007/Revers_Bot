package reversbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:/vk.properties", ignoreResourceNotFound = true)
@Slf4j
public class Main {


	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);


		log.info("Main class ok");

	}

}
