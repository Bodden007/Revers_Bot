package reversbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import reversbot.services.VkBot;


@Configuration
@EnableScheduling

@PropertySource(value = "classpath:/vk.properties", ignoreResourceNotFound = true)
public class InjectionContext {


    @Bean
    public VkBot vkBot() {
        return new VkBot();
    }





}