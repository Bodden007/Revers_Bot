package reversbot.services;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;

@Service
public class VkBot<jsonString> {

    @Value("${APP_ID}")
    int APP_ID;

    @Value("${access_token}")
    String access_token;
    private String getText;

    @Value("${own_Id}")
    int own_Id;

    //    @Scheduled(fixedDelay = 5000)

    public int vkb() throws ClientException, ApiException, IOException {

        Integer numberGet = 0;
        String typeAttPost = "post";
        String typeAttPhoto = "photo";
        String typeAttVideo = "video";
        String typeAtt = null;
        String urlPhoto = null;
        String jsonString = null;
        String fileName = "TestImage";
        GetResponse getResponse = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedWriter writer = null;



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
            System.out.println("NO HTTP");
            return 0;
        }


        //Начало основного цикла.

        for ( numberGet = 0; numberGet < 5; numberGet ++) {

        // определения типа сообщения text, video, post

            try {
                typeAtt = String.valueOf(getResponse.getItems().get(numberGet).getAttachments().get(0).getType());
            } catch (RuntimeException e) {
                typeAtt = String.valueOf(getResponse.getItems().get(1).getPostType());
                System.out.println(numberGet  + " " + typeAtt);
            }

        // чтение в переменную jsonString, text из JSON

            try {
                jsonString = getResponse.getItems().get(numberGet).getText();
                writer = new BufferedWriter(new FileWriter("src/text/VkText" + numberGet + ".txt"));
                if (numberGet.equals(0)) {
                    writer.write(jsonString);
                }else {
                        writer.append(jsonString);
                    }

            } catch (RuntimeException e) {
                System.out.println(numberGet + " NO TEXT");
                return 0;
            } finally {
                writer.close();
            }

            if (typeAtt.equals(typeAttPost)) {

                System.out.println(numberGet + " " + typeAttPost);

        // Если тип сообщения text, вытаскиваем из него картинки

            } else if (typeAtt.equals(typeAttPhoto)) {
                System.out.println(numberGet + " " + typeAttPhoto);
                try {
                    urlPhoto = String.valueOf(getResponse.getItems().get(numberGet).getAttachments().get(0).getPhoto().getSizes().get(8).getUrl());
                    System.out.println(urlPhoto);
                } catch (RuntimeException e) {
                    urlPhoto = String.valueOf(getResponse.getItems().get(numberGet).getAttachments().get(8).getPhoto().getSizes().get(8).getUrl());
                    System.out.println("NO IMAGE");

                }

            // сохраняем картинки локально

                try {
                    bufferedInputStream = new BufferedInputStream(new URL(urlPhoto).openStream());
                    fileOutputStream = new FileOutputStream("src/image/"+ fileName + numberGet + ".png");
                    byte data[] = new byte[1024];
                    int count;
                    while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
                        fileOutputStream.write(data, 0, count);
                        fileOutputStream.flush();
                    }
                } catch (RuntimeException e) {
                    System.out.println("NO CONNECT");
                    return 0;
                } finally {
                    bufferedInputStream.close();
                    fileOutputStream.close();
                }
            // Если тип сообщения video, удаляем фото

            } else if (typeAtt.equals(typeAttVideo)) {
                File file = new File("src/image/"+ fileName + numberGet + ".png");
                file.delete();
                System.out.println(numberGet + " It is video");

            } else {
                System.out.println("NO ATTACHMENT");
            }
        }

        System.out.println("........ОТСЮДА.........");
        System.out.println(getResponse);
        System.out.println(".........СЮДА........");
        System.out.println(getResponse.getItems().get(0).getText());
        System.out.println("$-----------$-----------$");
        System.out.println(")))))))))))))))))");
        System.out.println(urlPhoto);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(typeAtt);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(jsonString);
        System.out.println("))))))))))))))))))))))");

        return 0;
    }
}



