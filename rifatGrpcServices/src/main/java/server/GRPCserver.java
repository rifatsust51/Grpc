package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import user.UserService;

import java.io.IOException;

public class GRPCserver {
    public static void main(String[]  args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(1092).addService( new UserService()).build();
        server.start();
        System.out.println("Server started at " + server.getPort());

        server.awaitTermination();

    }
}
