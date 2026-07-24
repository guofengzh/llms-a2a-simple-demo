package llms.devs.a2a.client;

import org.a2aproject.sdk.client.ClientBuilder;
import org.a2aproject.sdk.client.ClientEvent;
import org.a2aproject.sdk.client.MessageEvent;
import org.a2aproject.sdk.client.http.A2ACardResolver;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransport;
import org.a2aproject.sdk.client.transport.jsonrpc.JSONRPCTransportConfig;
import org.a2aproject.sdk.client.transport.spi.ClientTransportConfig;
import org.a2aproject.sdk.jsonrpc.common.json.JsonUtil;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.Message;
import org.a2aproject.sdk.spec.Part;
import org.a2aproject.sdk.spec.TextPart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A simple example of using the A2A Java SDK to communicate with an A2A server.
 * This example is equivalent to the Python example provided in the A2A Python SDK.
 */
public class Client {

    private static final String MESSAGE_TEXT = "how much is 10 USD in INR?";

    private int port;

    public static void main(String...args) {
        String baseUrl = "http://localhost:" + 8080;

        org.a2aproject.sdk.client.Client client = null;
        try {
            AgentCard card = A2ACardResolver.builder().baseUrl(baseUrl).build().getAgentCard();
            System.out.println("Successfully fetched public agent card:");
            System.out.println(JsonUtil.toJson(card));
            System.out.println("Using public agent card for client initialization (default).");

            final CompletableFuture<String> messageResponse = new CompletableFuture<>();

            // Create consumers list for handling client events
            List<BiConsumer<ClientEvent, AgentCard>> consumers = new ArrayList<>();
            consumers.add((event, agentCard) -> {
                if (event instanceof MessageEvent messageEvent) {
                    Message responseMessage = messageEvent.getMessage();
                    StringBuilder textBuilder = new StringBuilder();
                    if (responseMessage.parts() != null) {
                        for (Part<?> part : responseMessage.parts()) {
                            if (part instanceof TextPart textPart) {
                                textBuilder.append(textPart.text());
                            }
                        }
                    }
                    messageResponse.complete(textBuilder.toString());
                } else {
                    System.out.println("Received client event: " + event.getClass().getSimpleName());
                }
            });

            // Create error handler for streaming errors
            Consumer<Throwable> streamingErrorHandler = (error) -> {
                System.err.println("Streaming error occurred: " + error.getMessage());
                error.printStackTrace();
                messageResponse.completeExceptionally(error);
            };

            ClientBuilder clientBuilder = org.a2aproject.sdk.client.Client
                    .builder(card)
                    .addConsumers(consumers)
                    .streamingErrorHandler(streamingErrorHandler);
            configureTransport(clientBuilder);
            client = clientBuilder.build();

            //Message message = A2A.toUserMessage(MESSAGE_TEXT); // the message ID will be automatically generated for you
            Message message = new Message(Message.Role.ROLE_USER,
                    List.of( new TextPart(MESSAGE_TEXT)),
                    "messageId-1",
                    null, //"contextId-1",
                    null, //"taskId-1",
                    List.of(), Map.of(), List.of());

            try {
                System.out.println("Sending message: " + MESSAGE_TEXT);
                client.sendMessage(message);
                System.out.println("Message sent successfully. Responses will be handled by the configured consumers.");

                String responseText = messageResponse.get();
                System.out.println("Response: " + responseText);
            } catch (Exception e) {
                System.err.println("Failed to get response: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (client != null)
                client.close();
        }
    }

    private static void configureTransport(ClientBuilder clientBuilder) {
        ClientTransportConfig transportConfig;
        switch(System.getProperty("quarkus.agentcard.protocol", "JSONRPC")) {
            case "JSONRPC":
            default:
                transportConfig = new JSONRPCTransportConfig();
                clientBuilder.withTransport(JSONRPCTransport.class, transportConfig);
                break;
        }
    }
}
