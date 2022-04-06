package user;

import com.nahid.grpc.User;
import com.nahid.grpc.userGrpc;
import io.grpc.stub.StreamObserver;

import java.sql.*;

public class UserService extends userGrpc.userImplBase {
    String url = "jdbc:mysql://localhost:3306/rifatgrpcservices";
    String username = "root";
    String pass = "";

    //Class.forName("com.mysql.jdbc.Driver");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;

    boolean isUserNameExist(String userName) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);

        try{
            statement = connection.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * FROM `user` WHERE 1");

        while(resultSet.next()){
            if(resultSet.getString("User Name").equals(userName)){
                return true;
            }
        }
        return false;
    }

    boolean isEmailExist(String email) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);
        try{
            statement = connection.createStatement();
        }catch (SQLException e){
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * FROM `user` WHERE 1");
        while(resultSet.next()){
            if(resultSet.getString("Email").equals(email)){
                return true;
            }
        }
        return false;
    }

    boolean isUserExists(String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, pass);

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet resultSet = statement.executeQuery("SELECT * FROM `user` WHERE 1");

        while (resultSet.next()) {
            if (resultSet.getString("User Name").equals(userName) && resultSet.getString("Password").equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registration(User.RegistrationRequest request, StreamObserver<User.APIResponse> responseObserver) {
        System.out.println("inside Registration");
        String userName = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();

        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        try{
            if(isEmailExist(email)){
                System.out.println("This email is already has an account");
                response.setResponseCode(0).setResponsemessage("Registration Unsuccessful, try with another email.");
            }else if(isUserNameExist(userName)){
                System.out.println("This username already exist");
                response.setResponseCode(0).setResponsemessage("Try another username");
            }else{
                statement.executeUpdate("INSERT INTO `user`(`User Name`, `Email`, `Password`) VALUES ('"+userName+"','"+email+"','"+password+"')");
                System.out.println("Registration Successful");
                response.setResponseCode(0).setResponsemessage("Registration Successful");

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

       System.out.println();

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }

    @Override
    public void login(User.LoginRequest request, StreamObserver<User.APIResponse> responseObserver) {
        System.out.println("An user is trying to login");
        String userName = request.getUsername();
        String password = request.getPassword();

        User.APIResponse.Builder response = User.APIResponse.newBuilder();

        try{
            if(isUserExists(userName, password)){
                response.setResponseCode(0).setResponsemessage("Login Successful");
                System.out.println("Login successful");
            }
            else{
                response.setResponseCode(1).setResponsemessage("Invalid username or password//user doesnot exist with this info.");
                System.out.println("Login unsuccessful");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.Empty request, StreamObserver<User.APIResponse> responseObserver) {
        //super.logout(request, responseObserver);
        System.out.println("A user has logged out.");
        User.APIResponse.Builder response = User.APIResponse.newBuilder();
response.setResponsemessage("An user has logged out.").setResponseCode(0);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
