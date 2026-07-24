## Usage

1. First, start the server by running `llms.devs.a2a.server.ServerApplication`.
2. Then, start `llms.devs.a2a.client.Client` to interact with the server.

## TODO

We are currently blocked by the following error:
```text
Failed to get response: Could not find a Task/Message for 385f95c2-744a-4c37-bc34-490ec2f9b8c4: {@type=type.googleapis.com/google.rpc.ErrorInfo, reason=INTERNAL, domain=a2a-protocol.org, metadata={}}
```

## Note

This is a demonstration of a simple integration of the `A2A Java SDK` with a `Spring Boot 4`, used to verify potential issues encountered when integrating with `A2A Java SDK 1.1.0.Final`.

Currently, there are several integration solutions between the `A2A Java SDK` and `Spring Boot`, but most of them use `A2A Java SDK 0.3.3.Final`. Only the [A2A Java SDK for Spring Framework](https://github.com/goodfriend2ks/a2a-java-sdk-spring) uses `A2A Java SDK 1.0.0.Alpha1`, which is closer to the latest official version of `A2A Java SDK`, `1.1.0.Final`.

This work is currently based on [A2A Java SDK for Spring Framework](https://github.com/goodfriend2ks/a2a-java-sdk-spring). We are trying to make some of its code (JSONRPC) run on `A2A Java SDK 1.1.0.Final`.
