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
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;

@Service
@Controller
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

        String urlPhoto = null;
        String jsonString = null;
        String fileName = "TestImage.png";
        GetResponse getResponse = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedWriter writer = null;

        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            UserActor actor = new UserActor(APP_ID, access_token);
            getResponse = vk.wall().get(actor)
                    .ownerId(-26493942)
                    .count(1)
                    .offset(5)
                    .filter(GetFilter.valueOf("ALL"))
                    .execute();
        }catch (RuntimeException e) {
            System.out.println("NO HTTP");
            return 0;
        }

        try {
            jsonString = getResponse.getItems().get(0).getText();
            writer = new BufferedWriter(new FileWriter("VkText.txt"));
            writer.write(jsonString);
        } catch (RuntimeException e) {
            System.out.println("NO TEXT");
            return 0;
        } finally {
            writer.close();
        }

        try {
            urlPhoto = String.valueOf(getResponse.getItems().get(0).getAttachments().get(0).getPhoto().getSizes().get(8).getUrl());
        } catch (RuntimeException e) {
            System.out.println("NO IMAGE");
            return 0;
        }

        try {
            bufferedInputStream = new BufferedInputStream(new URL(urlPhoto).openStream());
            fileOutputStream = new FileOutputStream(fileName);
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

        System.out.println("........ОТСЮДА.........");
        System.out.println(getResponse);
        System.out.println(".........СЮДА........");
        System.out.println(getResponse.getItems().get(0).getText());
        System.out.println("$-----------$-----------$");
        System.out.println(")))))))))))))))))");
        System.out.println(urlPhoto);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%");

        return 0;
    }
}



