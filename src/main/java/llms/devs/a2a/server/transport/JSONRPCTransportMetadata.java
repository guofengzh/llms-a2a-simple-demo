package llms.devs.a2a.server.transport;

import org.a2aproject.sdk.server.TransportMetadata;
import org.a2aproject.sdk.spec.TransportProtocol;
import org.jspecify.annotations.NullMarked;

public class JSONRPCTransportMetadata implements TransportMetadata {
    @Override
    @NullMarked
    public String getTransportProtocol() {
        return TransportProtocol.JSONRPC.asString();
    }
}