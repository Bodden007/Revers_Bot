package reversbot.services;


import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


@Service

public class VkBot {

    @Value("${APP_ID}")
    int APP_ID;

    @Value("${access_token}")
    String access_token;



//    @Scheduled(fixedDelay = 5000)
    @PostConstruct
    public void vkb() throws ClientException, ApiException {

        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);


        UserActor actor = new UserActor(APP_ID, access_token);

        GetResponse getResponse = vk.wall().get(actor)
                .ownerId(-206163403)
                .count(1)
                .offset(5)
                .execute();


        System.out.println("........FROM HERE.........");
        System.out.println(getResponse);
        System.out.println(".........HERE........");
    }

}
