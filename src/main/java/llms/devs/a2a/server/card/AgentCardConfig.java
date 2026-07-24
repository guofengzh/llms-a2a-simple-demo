package llms.devs.a2a.server.card;

import org.a2aproject.sdk.spec.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class AgentCardConfig {

    @Bean
    public AgentCard agentCard() {
        return AgentCard.builder()
                .name("Hello World Agent")
                .description("Just a hello world agent")
                .version("1.1.0")
                .capabilities(AgentCapabilities.builder()
                        .streaming(false)
                        .pushNotifications(false)
                        .build())
                .supportedInterfaces(
                        Collections.singletonList(new AgentInterface("JSONRPC", "http://localhost:8080"))
                )
                .defaultInputModes(Collections.singletonList("text"))
                .defaultOutputModes(Collections.singletonList("text"))
                .skills(Collections.singletonList(AgentSkill.builder()
                        .id("hello_world")
                        .name("Returns hello world")
                        .description("just returns hello world")
                        .tags(Collections.singletonList("hello world"))
                        .examples(List.of("hi", "hello world"))
                        .build()))
                .build();
    }
}
