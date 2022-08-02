package reversbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reversbot.services.VkBot;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);
		VkBot vkBot = context.getBean(VkBot.class);
		vkBot.hello();
	}

}
