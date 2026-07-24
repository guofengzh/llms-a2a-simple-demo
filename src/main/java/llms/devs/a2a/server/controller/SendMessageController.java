package llms.devs.a2a.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.a2aproject.sdk.common.A2AHeaders;
import org.a2aproject.sdk.grpc.utils.JSONRPCUtils;
import org.a2aproject.sdk.jsonrpc.common.json.JsonProcessingException;
import org.a2aproject.sdk.jsonrpc.common.wrappers.SendMessageRequest;
import org.a2aproject.sdk.jsonrpc.common.wrappers.SendMessageResponse;
import org.a2aproject.sdk.server.ServerCallContext;
import org.a2aproject.sdk.server.auth.UnauthenticatedUser;
import org.a2aproject.sdk.transport.jsonrpc.context.JSONRPCContextKeys;
import org.a2aproject.sdk.transport.jsonrpc.handler.JSONRPCHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SendMessageController {

    private final JSONRPCHandler jsonRpcRequestHandler;

    // The SDK provides the default handler bean when configuring the reference server module
    public SendMessageController(JSONRPCHandler jsonRpcRequestHandler) {
        this.jsonRpcRequestHandler = jsonRpcRequestHandler;
    }

    /**
     * Primary A2A Communication Endpoint.
     * Processes JSON-RPC 2.0 requests such as "SendMessage".
     */
    @PostMapping(
            path = {"", "/"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> handleSendMessageRpc(
            @RequestBody String rawJsonRpcRequest,
            HttpServletRequest httpServletRequest) throws JsonProcessingException {
        // Hard-coded the version
        final String version = "1.0";
        var headers = new HashMap<String, String>();
        headers.put(A2AHeaders.A2A_VERSION, version);
        var state = Map.<String, Object>of(
                JSONRPCContextKeys.HEADERS_KEY, headers,
                JSONRPCContextKeys.METHOD_NAME_KEY, "SendMessage"
        );

        ServerCallContext context = new ServerCallContext(UnauthenticatedUser.INSTANCE, state, Set.of(), version);
        context.getState().put(JSONRPCContextKeys.METHOD_NAME_KEY, "SendMessage");

        var request = JSONRPCUtils.parseRequestBody(rawJsonRpcRequest, null);
        try {
            // The SDK handler reads the payload, routes "SendMessage",
            // runs business handler logics, and spits back the structured JSON-RPC result.
            SendMessageResponse jsonRpcResponse = jsonRpcRequestHandler.onMessageSend((SendMessageRequest)request, context);

            return ResponseEntity.ok(jsonRpcResponse);
        } catch (Exception e) {
            // Fallback JSON-RPC internal error processing (-32603)
            String errorResponse = String.format(
                    "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"Internal A2A Error: %s\"},\"id\":null}",
                    e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
