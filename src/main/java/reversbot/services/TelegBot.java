package reversbot.services;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
@Component
@Slf4j
@Getter
@Setter
public class TelegBot {
    @Value("${telegram_token}")
    String telegram_token;
    @Value("${chat_id}")
    String chat_id;
    private String urlTelegram;

    public void sendHi (){
        urlTelegram = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=<em>%s</em>&parse_mode=html";

        urlTelegram = String.format(urlTelegram, telegram_token, chat_id, "ПОДСЛУШАННО РЕЖ \uD83D\uDE48\uD83D\uDE49\uD83D\uDE4A");

        sendURL(urlTelegram);
    }
    public void sendMessage (String message){

        urlTelegram = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        try {
            urlTelegram = String.format(urlTelegram, telegram_token, chat_id, URLEncoder.encode(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendURL(urlTelegram);
    }
    public void sendPhoto (String urlPhoto, String message) {

        urlTelegram = "https://api.telegram.org/bot%s/sendPhoto?chat_id=%s&photo=%s&caption=%s";
        try {
            urlTelegram = String.format(urlTelegram, telegram_token, chat_id
                    , URLEncoder.encode(urlPhoto, "UTF-8"), URLEncoder.encode(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendURL(urlTelegram);
    }

    public void sendMediaGroup(String jsonString){
        String urlTelegram =("https://api.telegram.org/bot%s/sendMediaGroup?chat_id=%s&media=%s");
        try {
            urlTelegram = String.format(urlTelegram, telegram_token, chat_id
                    , URLEncoder.encode(jsonString, "UTF-8"));
        } catch (RuntimeException | UnsupportedEncodingException e) {

            log.error("SENDMEDIAGROUP FAILED");
        }
        sendURL(urlTelegram);
    }
    public void sendURL(String urlTelegram) {

        try {
            URL url = new URL(urlTelegram);
            URLConnection connection = url.openConnection();
            InputStream inputStream = new ByteArrayInputStream(connection.getInputStream().readAllBytes());

        }catch (RuntimeException | IOException e){

            log.error("URL FAILED");
            e.printStackTrace();
        }

    }
}
