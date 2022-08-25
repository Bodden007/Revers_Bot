package reversbot.services;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.apache.commons.io.FileUtils;
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

    static int [] postIdOld = new int[5];
    static int [] postId = new int[5];

    static String [] urlPhoto = new String[10];

    static int phelp = 0;

    static String typeAtt = "null";
    static String typeAttPhoto = "photo";
    static String typeAttVideo = "video";



    //    @Scheduled(fixedDelay = 5000)

    public int vkb() throws ClientException, ApiException, IOException {

        String text = null;
        Integer numberGet = 0;
        String post_sours = null;
        String jsonString = null;
        String fileName = "TestImage";
        GetResponse getResponse = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Чистим кэш удаляем из деректории text & image

//        FileUtils.cleanDirectory(new File("src/cache/text"));
//        FileUtils.cleanDirectory(new File("src/cache/image"));
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
            System.out.println("NO HTTP");
            return 0;
        }

        // Забераем данные ID с cache для сравнения

//        try {
//            reader = new BufferedReader(new FileReader("src/cache/oldId/OldId.txt"));
//            for (int i = 0; i <= 4; i ++) {
//                postIdOld [i] = Integer.parseInt(reader.readLine());
//            }
//        } catch (RuntimeException e) {
//            System.out.println("reader faile");
//        } finally {
//            reader.close();
//        }
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
                    // определение наличие text. Если text нет, сообщение игнорируем

                    // Test
                    System.out.println(numberGet + "  ID " + postId[numberGet]);
                    //Test

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                         Определяем тип вложений PHOTO & VIDEO
                    try {
                        typeAtt = String.valueOf(getResponse.getItems().get(numberGet)
                                .getAttachments().get(0).getType());
                        System.out.println("It's  " + typeAtt);
                    }catch (RuntimeException e) {
                        System.out.println("The type is not defined");
                    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                    try {
                        text = getResponse.getItems().get(numberGet).getText();
                    } catch (RuntimeException e) {
                        text = null;
                    }


                        if (!(text.equals(""))) {

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        System.out.println("ID " + postId[numberGet] + " Text Ok! -- " + text);

                        // чтение в переменную jsonString, text из JSON
                        //считываем сообщение в файл

//                        try {
//                            jsonString = getResponse.getItems().get(numberGet).getText();
//                            writer = new BufferedWriter(new FileWriter("src/cache/text/VkText" + numberGet + ".txt"));
//                            if (numberGet.equals(0)) {
//                                writer.write(jsonString);
//                            } else {
//                                writer.append(jsonString);
//                            }
//                        } catch (RuntimeException e) {
//                            System.out.println(numberGet + " NO TEXT");
//                            return 0;
//                        } finally {
//                            writer.close();
//                        }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        // Если есть photo копируем URL photo

                        if (typeAtt.equals(typeAttPhoto)) {
                            for (int i = 0; i < 20; i++) {
                                try {
                                    urlPhoto[i] = String.valueOf(getResponse.getItems().get(numberGet)
                                            .getAttachments().get(i).getPhoto()
                                            .getSizes().get(8).getUrl());
                                    phelp = phelp + 1;
                                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                                    System.out.println(i);
                                    System.out.println(urlPhoto[i]);
                                    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                                } catch (RuntimeException e) {
                                    System.out.println("Quantity IMAGE -  " + phelp);
                                    break;
                                }
                            }

                          //   Если есть video копируем URL video
                        }else if (typeAtt.equals(typeAttVideo)) {

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                         // это заглушка video
                            System.out.println("It's video");
                        }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                                // если картинка есть сохраняем локально

                         if (typeAtt.equals(typeAttPhoto)) {

                             // TEST
                             System.out.println(postId[numberGet] + "  Image Yes");
                             // TEST

                            for ( int i = 0; i <= (phelp - 1); i++) {

//                                try {
//                                    bufferedInputStream = new BufferedInputStream(new URL(urlPhoto[i]).openStream());
//                                    fileOutputStream = new FileOutputStream("src/cache/image/" + fileName
//                                            + numberGet + "_" + i + ".png");
//                                    byte data[] = new byte[1024];
//                                    int count;
//                                    while ((count = bufferedInputStream.read(data, 0, 1024)) != -1) {
//                                        fileOutputStream.write(data, 0, count);
//                                        fileOutputStream.flush();
//                                    }
//                                } catch (RuntimeException e) {
//                                    System.out.println("Saving error");
//                                    break;
//                                } finally {
//                                    bufferedInputStream.close();
//                                    fileOutputStream.close();
//                                }
                            }
                        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        }else {
                        System.out.println("Post  " + postId[numberGet] + "  Out");
                        }

                }else{
                    postId[numberGet] = 0;
                    System.out.println(numberGet + "  Old message");
             }
        }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                // Запись ID сообщений в файл

//        try {
//            writer = new BufferedWriter(new FileWriter("src/cache/oldId/OldId.txt"));
//            for (int i = 0; i <= 4; i ++) {
//                writer.write(Integer.toString(postId[i]));
//                writer.newLine();
//            }
//        }catch (RuntimeException e) {
//            System.out.println("ERROR SAVING");
//
//        }finally {
//            writer.flush();
//            writer.close();
//        }
    return 0;
   }
}



