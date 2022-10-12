package reversbot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.*;


@SpringBootApplication
@Slf4j
public class Main {

	static int [] id_group = new int[4];
	static Integer [] post_id_old = new Integer[5];
	static Integer [] post_id_new = new Integer[5];
	static String [] keyAtt = new String[2];
	static int quantAttach = 0;

// AUXILIARY VARIABLES
	static int var;
	static Boolean  text;
	static Boolean video;
	static int numberGet = 0;
	static int phelp = -1;

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

		log.info("Group - " + var);
		for (int i = 0; i <= var-1; i++) {

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                          Reading the previous ones ID posts, saved local

			try {
				reader = new BufferedReader(new FileReader("src/group/id_post" + id_group[i] + ".txt"));
				for (int p = 0; p <= 4; p ++) {
					post_id_old[p] = Integer.parseInt(reader.readLine());
//					System.out.println(post_id_old[p]);
				}
			} catch (RuntimeException e) {
				log.error("reader failed");
			} finally {
				reader.close();
			}

			post = vkBot.http_client(id_group[i]);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//				json ID_post, text, type attachment, attachment

			for (int i1 = 0 ; i1 < 5; i1 ++) {

				post_id_new[i1] = Integer.valueOf(post.get(i1).get(0));

				if ((post.get(i1).get(1)) == "not"){
					text = false;
				}else {
					text = true;
				}

				keyAtt = (post.get(i1).get(2)).split("_");
				if (keyAtt[0].equals("video")){
					video = true;
				} else {
					video = false;
				}

				quantAttach = Integer.parseInt(keyAtt[1]);

				if ((post_id_new[i1] != post_id_old[0]) & (post_id_new[i1] != post_id_old[1])
						& post_id_new[i1] != post_id_old[2] & (post_id_new[i1] != post_id_old[3])
						& (post_id_new[i1] != post_id_old[4]) & text & !video) {

//					System.out.println(post_id_new[i1] + ", ATTACHMENT : " + post.get(i1).get(2));

					telegBot.sendHi();

					log.info(post_id_new[i1] + ":  ");

					try {
						if (quantAttach == 0){

							telegBot.sendMessage(post.get(i1).get(1));

						} else if (quantAttach == 1) {

							telegBot.sendPhoto(post.get(i1).get(3), post.get(i1).get(1));

								} else if (quantAttach > 1) {

									JsonArrayBuilder jsonHash = Json.createArrayBuilder();

									for (int i2 = 3; i2 <= quantAttach + 1; i2++) {

										jsonHash.add(Json.createObjectBuilder()
												.add("type", "photo")
												.add("media", (post.get(i1).get(i2))));
									}
									jsonHash.add(Json.createObjectBuilder()
											.add("type", "photo")
											.add("media", (post.get(i1).get(quantAttach + 2)))
											.add("caption", (post.get(i1).get(1))));

									JsonArray jsonString = jsonHash.build();

									System.out.println("JSON String: " + jsonString);

									telegBot.sendMediaGroup(String.valueOf(jsonString));

								} else {
									log.error(post_id_new[i1] + ":  ERROR TEXT OR ATTACHMENT");
								}
					}catch (RuntimeException e){
						log.error(post_id_new[i1] + ": ERROR ATTACHMENT");
					}



				}else {

				}
				quantAttach = Integer.parseInt(keyAtt[1]);

			}
		}

		log.info("Main class ok");

	}

}
