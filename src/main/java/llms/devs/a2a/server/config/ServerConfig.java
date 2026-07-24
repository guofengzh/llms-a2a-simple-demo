package llms.devs.a2a.server.config;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.config.A2AConfigProvider;
import org.a2aproject.sdk.server.config.DefaultValuesConfigProvider;
import org.a2aproject.sdk.server.events.InMemoryQueueManager;
import org.a2aproject.sdk.server.events.MainEventBus;
import org.a2aproject.sdk.server.events.MainEventBusProcessor;
import org.a2aproject.sdk.server.events.QueueManager;
import org.a2aproject.sdk.server.requesthandlers.DefaultRequestHandler;
import org.a2aproject.sdk.server.requesthandlers.RequestHandler;
import org.a2aproject.sdk.server.tasks.*;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.StreamingEventKind;
import org.a2aproject.sdk.spec.Task;
import org.a2aproject.sdk.transport.jsonrpc.handler.JSONRPCHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class ServerConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);

	@Bean
	public TaskStore taskStore() {
		return new InMemoryTaskStore();
	}

	@Bean
	public MainEventBus mainEventBus() {
		return new MainEventBus();
	}

	@Bean
	QueueManager queueManager(TaskStore taskStore, MainEventBus mainEventBus) {
		return new InMemoryQueueManager((TaskStateProvider) taskStore, mainEventBus);
	}

	@Bean
	public PushNotificationConfigStore pushNotificationConfigStore() {
		return new InMemoryPushNotificationConfigStore();
	}

	@Bean
	public PushNotificationSender pushNotificationSender() {
		return new PushNotificationSender() {
			@Override
			public void sendNotification(StreamingEventKind event, Task task) {
				logger.info("Push notification requested for task {} but sender is disabled", task.id());
			}
		};
	}

	@Bean
    public A2AConfigProvider configProvider() {
		return new DefaultValuesConfigProvider();
	}

	@Bean
	public RequestHandler requestHandler(AgentExecutor agentExecutor, TaskStore taskStore, QueueManager queueManager,
	                                     PushNotificationConfigStore pushConfigStore,
										 MainEventBus mainEventBus,
										 PushNotificationSender pushSender,
	                                     @Qualifier("a2aInternal") Executor executor) {

		return DefaultRequestHandler.create(agentExecutor, taskStore, queueManager, pushConfigStore,
				new MainEventBusProcessor(mainEventBus, taskStore, pushSender, queueManager),
				executor,
				executor);
	}

	@Bean
    JSONRPCHandler jsonrpcHandler(
			AgentCard agentCard,
			RequestHandler requestHandler,
			@Qualifier("a2aInternal") Executor executor
	) {
		return new JSONRPCHandler(agentCard, requestHandler, executor);
	}
}
