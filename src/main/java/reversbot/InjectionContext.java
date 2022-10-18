package reversbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;

@PropertySource(value = "classpath:/vk.properties", ignoreResourceNotFound = true)
@EnableScheduling
public class InjectionContext {

    @Bean
    public ControllerBot reverseBot(){
        return new ControllerBot();
    }

    @Bean
    public VkBot vkBot() {
        return new VkBot();
    }

    @Bean
    public TelegBot telegBot(){
        return new TelegBot();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

}