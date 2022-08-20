package reversbot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reversbot.services.VkBot;

@SpringBootApplication
public class Main {

	static String DataText;
	public String getDataText() {
		return DataText;
	}

	public void setDataText(String dataText) {
		DataText = dataText;
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);
		VkBot vkBot = context.getBean(VkBot.class);

		vkBot.vkb();

	}

}
