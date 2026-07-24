package llms.devs.a2a.server.executor;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.agentexecution.RequestContext;
import org.a2aproject.sdk.server.tasks.AgentEmitter;
import org.a2aproject.sdk.spec.A2AError;
import org.a2aproject.sdk.spec.UnsupportedOperationError;
import org.springframework.stereotype.Component;

@Component
public class AgentExecutorConfig implements AgentExecutor {
    @Override
    public void execute(RequestContext context, AgentEmitter emitter) throws A2AError {
        emitter.sendMessage("Hello World");
    }

    @Override
    public void cancel(RequestContext context, AgentEmitter emitter) throws A2AError {
        throw new UnsupportedOperationError();
    }
}
