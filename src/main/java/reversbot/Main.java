package reversbot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class Main {

	static int [] id_group = new int[4];
	static Integer [] post_id = new Integer[5];
	static int [] post_id_new = new int[5];
	static String [] keyAtt = new String[2];
	static int quantAttach = 0;
	static String  text = "null";
	static int numberGet = 0;
	static int phelp = -1;


// AUXILIARY VARIABLES
	static int var;

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext (InjectionContext.class);
		VkBot vkBot = context.getBean(VkBot.class);
		TelegBot telegBot = context.getBean(TelegBot.class);

		BufferedWriter writer = null;
		BufferedReader reader = null;

		ListMultimap<Integer, String> post = ArrayListMultimap.create();

		var = 0;

		try {
			reader = new BufferedReader (new FileReader("src/group/id_group.txt") );
				while (true){
					id_group[var] = Integer.parseInt(reader.readLine());
					if (id_group[var] >= 0000){
					break;
				}else {
					var ++;
				}
			}
		}catch (RuntimeException e){
			e.printStackTrace();
		}finally {
			reader.close();
		}
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		log.info("Group - " + var);
		for (int i = 0; i <= var-1; i++) {
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// Чистим кэш удаляем из деректории text & image

//			FileUtils.cleanDirectory(new File("src/cache/vk" + id_group[i] + "/text"));
//			FileUtils.cleanDirectory(new File("src/cache/vk" + id_group[i] +"/image"));
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//			System.out.println("var  - " + var);

//			System.out.println(id_group[i]);

			post = vkBot.http_client(id_group[i]);

//				json ID_post, text, type attachment, attachment

			for (int i1 = 0 ; i1 < 5; i1 ++) {
				if ((post.get(i1).get(1)) == "not"){
					text = "not";
				}else {
					text = "yes";
				}

				keyAtt = (post.get(i1).get(2)).split("_");

				System.out.println(post.get(i1).get(0) + " - text - "
						+ text + " - " + post.get(i1).get(1)
						+ " : Attachment - " + post.get(i1).get(2) );
				quantAttach = Integer.parseInt(keyAtt[1]);
				if(quantAttach > 0) {
					for (int i3 = 0; i3 <= quantAttach - 1; i3++) {
						System.out.println("URL: " + post.get(i1).get(3));
					}
				}
			}
		}










//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                          Проверяем пост - был или нет

//		try {
//			reader = new BufferedReader(new FileReader("src/group/id_post" + id_group[i] + ".txt"));
//			for (int p = 0; p <= 4; p ++) {
//				post_id[p] = Integer.parseInt(reader.readLine());
//				System.out.println(post_id[p]);
//			}
//		} catch (RuntimeException e) {
//			log.error("reader failed");
//		} finally {
//			reader.close();
//		}
//			log.error(String.valueOf(i));
//			System.out.println(vkBot.http_client(id_group[i]));

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++




//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!






		log.info("Main class ok");

	}

}
