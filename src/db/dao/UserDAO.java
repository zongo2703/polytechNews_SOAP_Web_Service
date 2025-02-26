package db.dao;
import domain.*;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    //connection function
    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mglsi_news","java","java");
    }

    //this function return admin's token basing on his id
    public String getToken(int id) throws SQLException{
        PreparedStatement sql = getConnection().prepareStatement("Select TokenValue from Token WHERE  IdUser=?");
        sql.setInt(1,id);
        ResultSet token = sql.executeQuery();
        token.next();
        return token.getString("TokenValue");
    }

    //this function return admin's token basing on his id
    public int getId(String token) throws SQLException{
        PreparedStatement sql = getConnection().prepareStatement("Select IdUser from Token WHERE  TokenValue=?");
        sql.setString(1,token);
        ResultSet id = sql.executeQuery();
        id.next();
        return id.getInt("IdUser");
    }
    //This function return login and password's user
    public String getUserPassword(String login) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select Password from Users WHERE  Login=?");
        sql.setString(1,login);
        ResultSet infos = sql.executeQuery();
        infos.next();
        return infos.getString("password");
    }

    //this function retrieves the list of users
    public ArrayList<User> getUsers() throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("Select * from Users");
        ResultSet userList = sql.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        Administrator admin = new Administrator();
        SimpleUser simpleUser = new SimpleUser();
        Editor editor = new Editor();

        while (userList.next()) {
            String login = userList.getString("login");
            String pwd = userList.getString("password");
            String type = userList.getString("type");
            switch (type) {
                case "administrateur":
                    admin.setLogin(login);
                    admin.setPassword(pwd);
                    users.add(admin);
                    break;

                case "editeur":
                    editor.setLogin(login);
                    editor.setPassword(pwd);
                    users.add(editor);
                    break;

                case "utilisateur simple":
                    simpleUser.setLogin(login);
                    simpleUser.setPassword(pwd);
                    users.add(simpleUser);
                    break;
            }
        }
        return users;
    }

    //this function add user in the database
    public void addUser(String login, String password, String type) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("INSERT INTO Users(Login,Password,Type) VALUES (?,?,?)");
        sql.setString(1,login);
        sql.setString(2,password);
        sql.setString(3,type);
        sql.executeUpdate();
    }

    //update function depending on user's id
    public void updateUser(String login, String password, String type,int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("UPDATE Users SET Login=?, Password=?, Type=? WHERE Id=? ");
        sql.setString(1,login);
        sql.setString(2,password);
        sql.setString(3,type);
        sql.setInt(4,id);
        sql.executeUpdate();
    }

    //delete function depending on user's login
    public void deleteUser(int id) throws SQLException {
        PreparedStatement sql = getConnection().prepareStatement("DELETE FROM Users WHERE Id=?");
        sql.setInt(1,id);
        sql.executeUpdate();
    }

}
