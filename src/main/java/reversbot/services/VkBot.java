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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Getter
@Setter
public class VkBot {

    @Value("${appId}")
    int appId;
 
    @Value("${accessToken}")
    private String accessToken;



    static int [] postId = new int[5];
    static int postIdNew = 0;
    static String [] urlPhoto = new String[10];
    static int quantAttach = 0;
    static String typeAtt = new String("null");
    static String typeAttPhoto = new String("photo");
    static String typeAttVideo = new String("video");

    GetResponse getResponse = null;
    static String text = "null";

    public ListMultimap<Integer, String> http_client(int ownId) throws ClientException, ApiException, IOException{

        Multimap<Integer, String> post = ArrayListMultimap.create();

        GetResponse getResponse = null;

        try {
            TransportClient transportClient = new HttpTransportClient();
            VkApiClient vk = new VkApiClient(transportClient);
            UserActor actor = new UserActor(appId, accessToken);
            getResponse = vk.wall().get(actor)
                    .ownerId(ownId)
                    .count(5)
                    .offset(0)
                    .filter(GetFilter.valueOf("ALL"))
                    .execute();
        }catch (RuntimeException e) {
            log.error("No HTTP", new Throwable());
        }

        for ( int i = 4; i >= 0; i--) {

            quantAttach = 0;

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
                            urlPhoto[i] = String.valueOf(getResponse.getItems().get(i)
                                    .getAttachments().get(i1).getPhoto()
                                    .getSizes().get(8).getUrl());
                            quantAttach = quantAttach + 1;

                    }
                } catch (RuntimeException e) {

                }

                //   Если есть video копируем URL video ( пока заглушка)
            }else if (typeAtt.equals(typeAttVideo)) {

            }

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
                log.error("NOT JSON");
            }

        }

        return (ListMultimap<Integer, String>) post;
    }

}



