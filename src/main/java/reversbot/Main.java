package reversbot;

import com.google.gson.JsonObject;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reversbot.services.VkBot;


import java.io.IOException;

@SpringBootApplication
public class Main {

	static String DataText;
	public String getDataText() {
		return DataText;
	}

	public void setDataText(String dataText) {
		DataText = dataText;
	}




	public static void main(String[] args) throws ClientException, ApiException, IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);
		VkBot vkBot = context.getBean(VkBot.class);





		vkBot.vkb();
		System.out.println("-----------");
		System.out.println("-----------");

		System.out.println("&&&&&&&&&&&");
//		System.out.println(jsonVkBot.toPrettyString(DataText));
		System.out.println("$$$$$$$$$$$");

	}

}
