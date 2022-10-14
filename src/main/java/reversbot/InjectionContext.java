package reversbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import reversbot.services.TelegBot;
import reversbot.services.VkBot;


@Configuration
@PropertySource(value = "classpath:/vk.properties", ignoreResourceNotFound = true)
public class InjectionContext {

    @Bean
    public ReverseBot reverseBot(){
        return new ReverseBot();
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