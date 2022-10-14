package reversbot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.*;
import java.util.Objects;



@SpringBootApplication
@ComponentScan("{reversbot}")
@Slf4j
public class Main {

//	static int [] id_group = new int[4];
//	static Integer [] post_id_old = new Integer[5];
//	static Integer [] post_id_new = new Integer[5];
//	static String [] keyAtt = new String[2];
//	static int quantAttach = 0;
//
//// AUXILIARY VARIABLES
//	static int var;
//	static Boolean  text;
//	static Boolean video;

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);
		ReverseBot reverseBot = context.getBean(ReverseBot.class);

		reverseBot.revBot();

		log.info("Main class ok");

	}

}
