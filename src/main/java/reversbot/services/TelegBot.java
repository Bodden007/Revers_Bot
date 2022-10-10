package reversbot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.json.Json;
import javax.json.JsonArray;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Component
@Slf4j
public class TelegBot {

    @Value("${telegram_token}")
    String telegram_token;

    @Value("${chat_id}")
    String chat_id;

    String urlTelegram =("https://api.telegram.org/bot%s/sendMediaGroup?chat_id=%s&media=%s");
//    string url_telegram = string.Format("https://api.telegram.org/bot{0}/sendMediaGroup?chat_id={1}",TOKEN, CHAT_ID);
//    private Object Json;

    public void telegb () throws IOException{

        String message = null;
//        String media = null;

        String img0 = "https://vk.com/rezh1?z=photo-49664936_457362884%2Fwall-49664936_634178";
        String img1 = "https://vk.com/rezh1?z=photo-49664936_457362885%2Fwall-49664936_634178";
        String testvideo = "https://vk.com/video-26493942_456300936";

        JsonArray value = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("type", "photo")
                        .add("media", img0))
                .add(Json.createObjectBuilder()
                        .add("type", "photo")
                        .add("media", img1)
                        .add("caption", testvideo))
//                .add(Json.createObjectBuilder()
//                        .add("type","video")
//                        .add("media", testvideo))
                .build();

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    //JSON


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


//        message = (chat_id + value);

//        System.out.println(message);
//        System.out.println(message);
        System.out.println(telegram_token);
        System.out.println(chat_id);

        urlTelegram = String.format(urlTelegram, telegram_token, chat_id, value);

        System.out.println(urlTelegram);



        try {
            URL url = new URL(urlTelegram);
            URLConnection connection = url.openConnection();
            InputStream inputStream = new ByteArrayInputStream(connection.getInputStream().readAllBytes());

        }catch (RuntimeException e){

            e.printStackTrace();
        }


    }
}
