package reversbot.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


@Service

public class VkBot {

    @Value("${ide}")
    String ide;

    @Value("${token}")
    int token;

    @Scheduled(fixedDelay = 5000)
    @PostConstruct
    public void hello(){

        System.out.println ("Hello BOSS!!!!");
        System.out.println("ide = " + ide);
        System.out.println("token = " + token);
        System.out.println("        ");
    }

}
