package org.viktor.phd.messaging;

import io.atomix.cluster.MemberId;
import io.atomix.cluster.messaging.MessagingService;
import io.atomix.primitive.session.SessionId;
import io.atomix.protocols.raft.protocol.*;
import io.atomix.utils.net.Address;
import io.atomix.utils.serializer.Serializer;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class RaftClientMessagingProtocol extends RaftMessagingProtocol implements RaftClientProtocol {

    public RaftClientMessagingProtocol(MessagingService messagingService, Serializer serializer, Function<MemberId, Address> addressProvider) {
        super(messagingService, serializer, addressProvider);
    }

    @Override
    public CompletableFuture<OpenSessionResponse> openSession(MemberId memberId, OpenSessionRequest request) {
        return sendAndReceive(memberId, "open-session", request);
    }

    @Override
    public CompletableFuture<CloseSessionResponse> closeSession(MemberId memberId, CloseSessionRequest request) {
        return sendAndReceive(memberId, "close-session", request);
    }

    @Override
    public CompletableFuture<KeepAliveResponse> keepAlive(MemberId memberId, KeepAliveRequest request) {
        return sendAndReceive(memberId, "keep-alive", request);
    }

    @Override
    public CompletableFuture<QueryResponse> query(MemberId memberId, QueryRequest request) {
        return sendAndReceive(memberId, "query", request);
    }

    @Override
    public CompletableFuture<CommandResponse> command(MemberId memberId, CommandRequest request) {
        return sendAndReceive(memberId, "command", request);
    }

    @Override
    public CompletableFuture<MetadataResponse> metadata(MemberId memberId, MetadataRequest request) {
        return sendAndReceive(memberId, "metadata", request);
    }

    @Override
    public void registerHeartbeatHandler(Function<HeartbeatRequest, CompletableFuture<HeartbeatResponse>> handler) {
        registerHandler("heartbeat", handler);
    }

    @Override
    public void unregisterHeartbeatHandler() {
        unregisterHandler("heartbeat");
    }

    @Override
    public void reset(Set<MemberId> members, ResetRequest request) {
        for (MemberId memberId : members) {
            sendAsync(memberId, String.format("reset-%d", request.session()), request);
        }
    }

    @Override
    public void registerPublishListener(SessionId sessionId, Consumer<PublishRequest> listener, Executor executor) {
        messagingService.registerHandler(String.format("publish-%d", sessionId.id()), (e, p) -> {
            listener.accept(serializer.decode(p));
        }, executor);
    }

    @Override
    public void unregisterPublishListener(SessionId sessionId) {
        messagingService.unregisterHandler(String.format("publish-%d", sessionId.id()));
    }
}
