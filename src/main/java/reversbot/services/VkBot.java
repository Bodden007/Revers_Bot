package reversbot.services;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;
import java.util.Optional;


@Service
@Slf4j
public class VkBot {

    @Value("${APP_ID}")
    int APP_ID;

    @Value("${access_token}")
    String access_token;
    private String getText;

    @Value("${own_Id}")
    int own_Id;

    static int [] postIdOld = new int[5];
    static int [] postId = new int[10];

    static String [] urlPhoto = new String[10];

    static int phelp = -1;

    static String typeAtt = new String("null");
    static String typeAttPhoto = new String("photo");
    static String typeAttVideo = new String("video");
    static String foundtext = null;
    static String message = "new";
    static String checkURl = null;



    //    @Scheduled(fixedDelay = 5000)

    public int vkb() throws ClientException, ApiException, IOException {

        String text = null;
        Integer numberGet = 0;
        String fileName = "TestImage";
        GetResponse getResponse = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Чистим кэш удаляем из деректории text & image

        FileUtils.cleanDirectory(new File("src/cache/vk" + own_Id + "/text"));
        FileUtils.cleanDirectory(new File("src/cache/vk" + own_Id +"/image"));
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            UserActor actor = new UserActor(APP_ID, access_token);
            getResponse = vk.wall().get(actor)
                    .ownerId(own_Id)
                    .count(5)
                    .offset(0)
                    .filter(GetFilter.valueOf("ALL"))
                    .execute();
        }catch (RuntimeException e) {
            log.error("No HTTP", new Throwable());
            return 0;
        }

        // Забераем данные ID с cache для сравнения

        try {
            reader = new BufferedReader(new FileReader("src/cache/vk" + own_Id + "/Id/Idold.txt"));
            for (int i = 0; i <= 4; i ++) {
                postIdOld [i] = Integer.parseInt(reader.readLine());
            }
        } catch (RuntimeException e) {
            log.error("reader faile");
        } finally {
            reader.close();
        }
        //Начало основного цикла.

            for ( numberGet = 4; numberGet >= 0; numberGet --) {

                phelp = 0;

                // чтение последнего ID из файла

                postId[numberGet] = getResponse.getItems().get(numberGet).getId();

                //сравнение ID на повторяемость запросов

                if ((postId[numberGet] > postIdOld[4]) & (postId[numberGet] > postIdOld[3])
                        & (postId[numberGet] > postIdOld[2]) & (postId[numberGet] > postIdOld[1])
                        & (postId[numberGet] > postIdOld[0])) {
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//                         Определяем тип вложений PHOTO & VIDEO
                    try {
                        if ((getResponse.getItems().get(numberGet)
                                .getAttachments()) != null) {
                        typeAtt = String.valueOf(getResponse.getItems().get(numberGet)
                                .getAttachments().get(0).getType());
                        }else {
                            typeAtt = "null";
                        }
                    }catch (RuntimeException e) {

                        e.printStackTrace();

                    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                    try {
                        text = getResponse.getItems().get(numberGet).getText();
                    } catch (RuntimeException e) {
                        log.error("error");
                    }

                    // определение наличие text. Если text нет, сообщение игнорируем

                        if (!(text.equals(""))) {

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        foundtext = "yes";

                        //считываем сообщение в файл

                        try {
                            writer = new BufferedWriter(new FileWriter("src/cache/vk" + own_Id
                                    + "/text/VkText" + "_" + numberGet + ".txt"));
                            if (numberGet.equals(0)) {
                                writer.write(text);
                            } else {
                                writer.append(text);
                            }
                        } catch (RuntimeException e) {
                            return 0;
                        } finally {
                            writer.flush();
                            writer.close();
                        }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        // Если есть photo копируем URL photo

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            // ПЕРЕДЕЛАТЬ

                        if (typeAtt.equals(typeAttPhoto)) {
                            try {
                            for ( int i = 0 ; i < urlPhoto.length; i++) {
                                Optional optional = Optional.ofNullable(getResponse.getItems().get(numberGet)
                                        .getAttachments());

                                if (optional.isPresent()) {
                                        urlPhoto[i] = String.valueOf(getResponse.getItems().get(numberGet)
                                                .getAttachments().get(i).getPhoto()
                                                .getSizes().get(8).getUrl());
                                        phelp = phelp + 1;

                                } else {
                                    log.info(postId[numberGet] + " - STOP NULL");
                                    break;}

                            }
                            } catch (RuntimeException e) {
//                                log.error(postId[numberGet] + " NPE PHOTO");
//
//                                e.printStackTrace();
                            }

                          //   Если есть video копируем URL video ( пока заглушка)
                        }else if (typeAtt.equals(typeAttVideo)) {

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         // это заглушка video
                            System.out.println("It's video");
                        }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                                // если картинка есть сохраняем локально

                         if (typeAtt.equals(typeAttPhoto)) {

                            for ( int i = 0; i <= (phelp - 1); i++) {

                                try {
                                    bufferedInputStream = new BufferedInputStream(new URL(urlPhoto[i]).openStream());
                                    fileOutputStream = new FileOutputStream("src/cache/vk" + own_Id + "/image/" + fileName
                                           + "_" + numberGet + "_" + i + ".png");
                                    byte data[] = new byte[1024];
                                    int count;
                                    while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
                                        fileOutputStream.write(data, 0, count);
                                        fileOutputStream.flush();
                                    }
                                } catch (RuntimeException e) {
                                    log.error("Saving error");
                                    break;
                                } finally {
                                    bufferedInputStream.close();
                                    fileOutputStream.close();
                                }
                            }
                        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        }else {
                        foundtext = "not";
                        }
//##############################################################################################
                }else{
                    message = "old";
             }

        log.info("GetResponse " + numberGet + ",ID Post " + postId[numberGet] + ", message " + message
                + ", text " + foundtext + ", attachment " + typeAtt
                + ", quantity IMAGE " + phelp);
        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                // Запись ID сообщений в файл

        try {
            writer = new BufferedWriter(new FileWriter("src/cache/vk" + own_Id + "/Id/Idold.txt"));
            for (int i = 0; i <= 4; i ++) {
                writer.write(Integer.toString(postId[i]));
                writer.newLine();
            }
        }catch (RuntimeException e) {
            log.error("ERROR SAVING");

        }finally {
            writer.flush();
            writer.close();
        }
    return 0;
   }
}



