package reversbot;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.*;
import java.util.Objects;

@Component
@Slf4j
public class ControllerBot {

    @Autowired
    public  VkBot vkBot;
    @Autowired
    public  TelegBot telegBot;

    static int [] idGroup = new int[] {-49664936, -184477562};
    static Integer [] [] postIdOld = new Integer[2] [5];
    static Integer [] postIdNew = new Integer[5];
    static String [] keyAtt = new String[2];
    static int quantAttach = 0;

    // AUXILIARY VARIABLES
    static int var = 2;
    static Boolean  text;
    static Boolean video;

        @Scheduled(fixedRate = 900000)
        public void contrBot() throws IOException, ClientException, ApiException {

//            BufferedWriter writer = null;
//            BufferedReader reader = null;

            ListMultimap<Integer, String> post = ArrayListMultimap.create();

//            var = 2;

//            try {
//                reader = new BufferedReader (new FileReader("src/group/idGroup.txt") );
//                while (true){
//                    idGroup[var] = Integer.parseInt(reader.readLine());
//                    if (idGroup[var] >= 0000){
//                        break;
//                    }else {
//                        var ++;
//                    }
//                }
//            }catch (RuntimeException | FileNotFoundException e){
//                log.error("Reading id group");
//                e.printStackTrace();
//            }finally {
//                reader.close();
//            }

            for (int i = 0; i <= var-1; i++) {

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                          Reading the previous ones ID posts, saved local

//                try {
//                    reader = new BufferedReader(new FileReader("src/group/id_post" + idGroup[i] + ".txt"));
//                    for (int p = 0; p <= 4; p ++) {
//                        postIdOld[p] = Integer.parseInt(reader.readLine());
//                    }
//                } catch (RuntimeException e) {
//                    log.error("Reader the id post failed");
//                } finally {
//                    reader.close();
//                }

                post = vkBot.http_client(idGroup[i]);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//				json ID_post, text, type attachment, attachment

                for (int i1 = 0 ; i1 < 5; i1 ++) {

                    postIdNew[i1] = Integer.valueOf(post.get(i1).get(0));

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

                    if ((!Objects.equals(postIdNew[i1], postIdOld[i][0])) & (!Objects.equals(postIdNew[i1], postIdOld[i][1]))
                            & !Objects.equals(postIdNew[i1], postIdOld[i][2]) & (!Objects.equals(postIdNew[i1], postIdOld[i][3]))
                            & (!Objects.equals(postIdNew[i1], postIdOld[i][4])) & text & !video) {

                        telegBot.sendHi();

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

                                telegBot.sendMediaGroup(String.valueOf(jsonString));

                            } else {
                                log.error(postIdNew[i1] + ":  ERROR TEXT OR ATTACHMENT");
                            }
                        }catch (RuntimeException e){
                            log.error(postIdNew[i1] + ": ERROR ATTACHMENT");
                        }

                        log.info("Post: " + postIdNew[i1] + ", text: " + text + ", attachment: " + quantAttach);

                    }else {

                        log.info("Post: " + postIdNew[i1] + "- FLY");
                    }

                }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                  Saving the id post in postIdOld

//                try {
//                    writer = new BufferedWriter(new FileWriter("src/group/id_post"
//                            + idGroup[i] + ".txt"));
//                    for (int i4 = 0; i4 <= 4; i4 ++) {
//                        writer.write(Integer.toString(postIdNew[i4]));
//                        writer.newLine();
//                    }
//                }catch (RuntimeException e) {
//                    log.error("ERROR SAVING");
//
//                }finally {
//                    writer.flush();
//                    writer.close();
//                }

                for (int i3 = 0; i3 < 5; i3 ++){


                    postIdOld [i][i3] = postIdNew[i3];

                    System.out.println("Post Id Old - " + postIdOld[i][i3]);
                }

            }

            log.info("ControllerBot class ok");

        }

    }



