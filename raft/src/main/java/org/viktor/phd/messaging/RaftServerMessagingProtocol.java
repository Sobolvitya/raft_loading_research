package org.viktor.phd.messaging;

import io.atomix.cluster.MemberId;
import io.atomix.cluster.messaging.MessagingService;
import io.atomix.primitive.session.SessionId;
import io.atomix.protocols.raft.protocol.*;
import io.atomix.utils.net.Address;
import io.atomix.utils.serializer.Serializer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Raft server messaging protocol.
 */
public class RaftServerMessagingProtocol extends RaftMessagingProtocol implements RaftServerProtocol {
    public RaftServerMessagingProtocol(MessagingService messagingService, Serializer serializer, Function<MemberId, Address> addressProvider) {
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
    public CompletableFuture<JoinResponse> join(MemberId memberId, JoinRequest request) {
        return sendAndReceive(memberId, "join", request);
    }

    @Override
    public CompletableFuture<LeaveResponse> leave(MemberId memberId, LeaveRequest request) {
        return sendAndReceive(memberId, "leave", request);
    }

    @Override
    public CompletableFuture<ConfigureResponse> configure(MemberId memberId, ConfigureRequest request) {
        return sendAndReceive(memberId, "configure", request);
    }

    @Override
    public CompletableFuture<ReconfigureResponse> reconfigure(MemberId memberId, ReconfigureRequest request) {
        return sendAndReceive(memberId, "reconfigure", request);
    }

    @Override
    public CompletableFuture<InstallResponse> install(MemberId memberId, InstallRequest request) {
        return sendAndReceive(memberId, "install", request);
    }

    @Override
    public CompletableFuture<TransferResponse> transfer(MemberId memberId, TransferRequest request) {
        return sendAndReceive(memberId, "transfer", request);
    }

    @Override
    public CompletableFuture<PollResponse> poll(MemberId memberId, PollRequest request) {
        return sendAndReceive(memberId, "poll", request);
    }

    @Override
    public CompletableFuture<VoteResponse> vote(MemberId memberId, VoteRequest request) {
        return sendAndReceive(memberId, "vote", request);
    }

    @Override
    public CompletableFuture<AppendResponse> append(MemberId memberId, AppendRequest request) {
        return sendAndReceive(memberId, "append", request);
    }

    @Override
    public void publish(MemberId memberId, PublishRequest request) {
        sendAsync(memberId, String.format("publish-%d", request.session()), request);
    }

    @Override
    public CompletableFuture<HeartbeatResponse> heartbeat(MemberId memberId, HeartbeatRequest request) {
        return sendAndReceive(memberId, "heartbeat", request);
    }

    @Override
    public void registerOpenSessionHandler(Function<OpenSessionRequest, CompletableFuture<OpenSessionResponse>> handler) {
        registerHandler("open-session", handler);
    }

    @Override
    public void unregisterOpenSessionHandler() {
        unregisterHandler("open-session");
    }

    @Override
    public void registerCloseSessionHandler(Function<CloseSessionRequest, CompletableFuture<CloseSessionResponse>> handler) {
        registerHandler("close-session", handler);
    }

    @Override
    public void unregisterCloseSessionHandler() {
        unregisterHandler("close-session");
    }

    @Override
    public void registerKeepAliveHandler(Function<KeepAliveRequest, CompletableFuture<KeepAliveResponse>> handler) {
        registerHandler("keep-alive", handler);
    }

    @Override
    public void unregisterKeepAliveHandler() {
        unregisterHandler("keep-alive");
    }

    @Override
    public void registerQueryHandler(Function<QueryRequest, CompletableFuture<QueryResponse>> handler) {
        registerHandler("query", handler);
    }

    @Override
    public void unregisterQueryHandler() {
        unregisterHandler("query");
    }

    @Override
    public void registerCommandHandler(Function<CommandRequest, CompletableFuture<CommandResponse>> handler) {
        registerHandler("command", handler);
    }

    @Override
    public void unregisterCommandHandler() {
        unregisterHandler("command");
    }

    @Override
    public void registerMetadataHandler(Function<MetadataRequest, CompletableFuture<MetadataResponse>> handler) {
        registerHandler("metadata", handler);
    }

    @Override
    public void unregisterMetadataHandler() {
        unregisterHandler("metadata");
    }

    @Override
    public void registerJoinHandler(Function<JoinRequest, CompletableFuture<JoinResponse>> handler) {
        registerHandler("join", handler);
    }

    @Override
    public void unregisterJoinHandler() {
        unregisterHandler("join");
    }

    @Override
    public void registerLeaveHandler(Function<LeaveRequest, CompletableFuture<LeaveResponse>> handler) {
        registerHandler("leave", handler);
    }

    @Override
    public void unregisterLeaveHandler() {
        unregisterHandler("leave");
    }

    @Override
    public void registerConfigureHandler(Function<ConfigureRequest, CompletableFuture<ConfigureResponse>> handler) {
        registerHandler("configure", handler);
    }

    @Override
    public void unregisterConfigureHandler() {
        unregisterHandler("configure");
    }

    @Override
    public void registerReconfigureHandler(Function<ReconfigureRequest, CompletableFuture<ReconfigureResponse>> handler) {
        registerHandler("reconfigure", handler);
    }

    @Override
    public void unregisterReconfigureHandler() {
        unregisterHandler("reconfigure");
    }

    @Override
    public void registerInstallHandler(Function<InstallRequest, CompletableFuture<InstallResponse>> handler) {
        registerHandler("install", handler);
    }

    @Override
    public void unregisterInstallHandler() {
        unregisterHandler("install");
    }

    @Override
    public void registerTransferHandler(Function<TransferRequest, CompletableFuture<TransferResponse>> handler) {
        registerHandler("transfer", handler);
    }

    @Override
    public void unregisterTransferHandler() {
        unregisterHandler("transfer");
    }

    @Override
    public void registerPollHandler(Function<PollRequest, CompletableFuture<PollResponse>> handler) {
        registerHandler("poll", handler);
    }

    @Override
    public void unregisterPollHandler() {
        unregisterHandler("poll");
    }

    @Override
    public void registerVoteHandler(Function<VoteRequest, CompletableFuture<VoteResponse>> handler) {
        registerHandler("vote", handler);
    }

    @Override
    public void unregisterVoteHandler() {
        unregisterHandler("vote");
    }

    @Override
    public void registerAppendHandler(Function<AppendRequest, CompletableFuture<AppendResponse>> handler) {
        registerHandler("append", handler);
    }

    @Override
    public void unregisterAppendHandler() {
        unregisterHandler("append");
    }

    @Override
    public void registerResetListener(SessionId sessionId, Consumer<ResetRequest> listener, Executor executor) {
        messagingService.registerHandler(String.format("reset-%d", sessionId.id()), (e, p) -> {
            listener.accept(serializer.decode(p));
        }, executor);
    }

    @Override
    public void unregisterResetListener(SessionId sessionId) {
        messagingService.unregisterHandler(String.format("reset-%d", sessionId.id()));
    }
}
