package reversbot.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class VkBot {

    @Value("${APP_ID}")
    int APP_ID;

    @Value("${access_token}")
    String access_token;

    @Value("${own_Id}")
    int own_Id;

    static int [] post_id = new int[5];
    static int post_id_new = 0;

    static String [] urlPhoto = new String[10];

    static int quantAttach = 0;

    static String typeAtt = new String("null");
    static String typeAttPhoto = new String("photo");
    static String typeAttVideo = new String("video");
    static String foundtext = null;
    static String message = "new";

    GetResponse getResponse = null;
    static String text = "null";

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // Метод HTTP подключения

    public ListMultimap<Integer, String> http_client(int own_Id) throws ClientException, ApiException, IOException{

        Multimap<Integer, String> post = ArrayListMultimap.create();

        GetResponse getResponse = null;

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
        }

        for ( int i = 4; i >= 0; i--) {


            quantAttach = 0;

//            try {
//                text = getResponse.getItems().get(i).getText();
//            } catch (RuntimeException e) {
//                log.error("text reader error");
//            }

            try {
                if ((getResponse.getItems().get(i).getText()) == null || (getResponse
                        .getItems().get(i).getText()).isEmpty() || (getResponse
                        .getItems().get(i).getText()).trim().isEmpty()) {
                    text = "not";
                }else {
                    text = (getResponse.getItems().get(i).getText());
                }
            }catch (RuntimeException e){
                log.error("String text error");
            }

            try {
                if ((getResponse.getItems().get(i)
                        .getAttachments()) != null) {
                    typeAtt = String.valueOf(getResponse.getItems().get(i)
                            .getAttachments().get(0).getType());
                }else {
                    typeAtt = "null";
                }
            }catch (RuntimeException e) {
                e.printStackTrace();
            }

            if (typeAtt == "photo") {
                try {
                    for ( int i1 = 0 ; i1 < 10; i1++) {
//                        Optional optional = Optional.ofNullable(getResponse.getItems().get(i)
//                                .getAttachments());
//                        if (optional.isPresent()) {
                            urlPhoto[i] = String.valueOf(getResponse.getItems().get(i)
                                    .getAttachments().get(i1).getPhoto()
                                    .getSizes().get(8).getUrl());
                            quantAttach = quantAttach + 1;

//                        } else {
//                                    log.info(" - STOP NULL");
//                            break;}

                    }
                } catch (RuntimeException e) {

                }

                //   Если есть video копируем URL video ( пока заглушка)
            }else if (typeAtt.equals(typeAttVideo)) {

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // это заглушка video
//                System.out.println("It's video");
            }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            typeAtt = typeAtt + "_" + quantAttach;

            try {
                post.put(i, String.valueOf(getResponse.getItems().get(i).getId()));
                post.put(i, text);
                post.put(i, typeAtt);
                if (quantAttach > 0){
                    quantAttach = quantAttach - 1;
                    if (quantAttach == 0){
                        post.put(i, String.valueOf(getResponse.getItems().get(i)
                        .getAttachments().get(0).getPhoto()
                        .getSizes().get(8).getUrl()));
                    }else {
                        for (int i2 = 0; i2 <= quantAttach; i2++){
                            post.put(i, String.valueOf(getResponse.getItems().get(i)
                            .getAttachments().get(i2).getPhoto()
                            .getSizes().get(8).getUrl()));
                        }
                    }
                }
            }catch (RuntimeException e){
//                log.error("NOT JSON");
            }


        }





        return (ListMultimap<Integer, String>) post;
    }


//    public int post_id_new_json (int numberGet) throws ClientException, ApiException, IOException{
//
//
//        try {
//            post_id_new = getResponse.getItems().get(numberGet).getId();
//        }catch (RuntimeException e){
//            log.error("trouble read JSON");
//            e.printStackTrace();
//        }
//        return post_id_new;
//    }

    public void vkb() throws ClientException, ApiException, IOException {

        String text = null;
        Integer numberGet = 0;
        String fileName = "TestImage";
        GetResponse getResponse = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        //Начало основного цикла.

//            for ( numberGet = 4; numberGet >= 0; numberGet --) {
//
//                phelp = 0;

                // чтение последнего ID из файла

//                post_id_new[numberGet] = getResponse.getItems().get(numberGet).getId();

                //сравнение ID на повторяемость запросов

//                if ((post_id_new[numberGet] > post_id[4]) & (post_id_new[numberGet] > post_id[3])
//                        & (post_id_new[numberGet] > post_id[2]) & (post_id_new[numberGet] > post_id[1])
//                        & (post_id_new[numberGet] > post_id[0])) {
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
//                            return 0;
                        } finally {
                            writer.flush();
                            writer.close();
                        }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        // Если есть photo копируем URL photo

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                            // ПЕРЕДЕЛАТЬ



//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                                // если картинка есть сохраняем локально

//                         if (typeAtt.equals(typeAttPhoto)) {
//
//                            for ( int i = 0; i <= (quantAttach - 1); i++) {
//
//                                try {
//                                    bufferedInputStream = new BufferedInputStream(new URL(urlPhoto[i]).openStream());
//                                    fileOutputStream = new FileOutputStream("src/cache/vk" + own_Id + "/image/" + fileName
//                                           + "_" + numberGet + "_" + i + ".png");
//                                    byte data[] = new byte[1024];
//                                    int count;
//                                    while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
//                                        fileOutputStream.write(data, 0, count);
//                                        fileOutputStream.flush();
//                                    }
//                                } catch (RuntimeException e) {
//                                    log.error("Saving error");
//                                    break;
//                                } finally {
//                                    bufferedInputStream.close();
//                                    fileOutputStream.close();
//                                }
//                            }
//                        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                        }else {
//                        foundtext = "not";
//                        }
//##############################################################################################
//                }else{
//                    message = "old";
//             }

//        log.info("GetResponse " + numberGet + ",ID Post " + post_id_new[numberGet] + ", message " + message
//                + ", text " + foundtext + ", attachment " + typeAtt
//                + ", quantity IMAGE " + phelp);
//        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                // Запись ID сообщений в файл

//        try {
//            writer = new BufferedWriter(new FileWriter("src/cache/vk" + own_Id + "/Id/Idold.txt"));
//            for (int i = 0; i <= 4; i ++) {
//                writer.write(Integer.toString(post_id_new[i]));
//                writer.newLine();
//            }
//        }catch (RuntimeException e) {
//            log.error("ERROR SAVING");
//
//        }finally {
//            writer.flush();
//            writer.close();
        }
//    return 0;
   }
}



