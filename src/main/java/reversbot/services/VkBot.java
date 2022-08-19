package reversbot.services;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;


@Service

public class VkBot<jsonString> {



    @Value("${APP_ID}")
    int APP_ID;

    @Value("${access_token}")
    String access_token;
    private String getText;


    //    @Scheduled(fixedDelay = 5000)


    public void vkb() throws ClientException, ApiException, IOException {

        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);


        UserActor actor = new UserActor(APP_ID, access_token);


        GetResponse getResponse = vk.wall().get(actor)
                .ownerId(-26493942)
                .count(1)
                .offset(5)
                .filter(GetFilter.valueOf("ALL"))
                .execute();



        String jsonString = getResponse.getItems().get(0).getText();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();



//        WallpostFull wallpostFull = gson.fromJson(jsonString, WallpostFull.class);





        System.out.println("........ОТСЮДА.........");
        System.out.println(getResponse);
        System.out.println(".........СЮДА........");
        System.out.println(getResponse.getItems().get(0).getText());
        System.out.println("$-----------$-----------$");
        System.out.println(")))))))))))))))))");
        System.out.println(jsonString);



    }


}



