import com.nahid.grpc.User;
import com.nahid.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.google.protobuf.*;

import java.util.Scanner;

public class GrpcClient {
    public static void main(String args[]){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 1092).usePlaintext().build();
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);

        Scanner scr = new Scanner(System.in);

        while(true) {

            System.out.println("\nEnter 0 to Sign up" + "\t\t\t" + "Enter 1 to Sign in" + "\t\t\t" + "Enter 2 to log out");
            System.out.println("-------------------" + "\t\t\t" + "-------------------" + "\t\t\t" + "------------------");

            String command = scr.nextLine();
            if (command.equals("2")) {
                User.Empty emptyMessage = User.Empty.newBuilder().build();
                //User.APIResponse logout = userStub.logout(emptyMessage);
                User.APIResponse logout = userStub.logout(emptyMessage);
                System.out.println(logout.getResponsemessage());
                break;
            }
            if (command.equals("0")) {
                System.out.print("Email: ");
                String email = scr.nextLine();

                System.out.print("User Name: ");
                String userName = scr.nextLine();

                System.out.print("Password: ");
                String password = scr.nextLine();

                User.RegistrationRequest registration = User.RegistrationRequest.newBuilder().setEmail(email).setUsername(userName).setPassword(password).build();
                User.APIResponse regResponse = userStub.registration(registration);
                System.out.println(regResponse.getResponsemessage());

            } else if (command.equals("1")) {
                System.out.print("User Name: ");
                String userName = scr.nextLine();

                System.out.print("Password: ");
                String password = scr.nextLine();

                User.LoginRequest loginRequest = User.LoginRequest.newBuilder().setUsername(userName).setPassword(password).build();
                User.APIResponse response = userStub.login(loginRequest);
                System.out.println(response.getResponsemessage());

            } else {
                System.out.println("[Invalid command. Try again...]");
            }

        }
    }
}
