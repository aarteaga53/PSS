import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {

    Scanner kb = new Scanner(System.in);
    String username;
    String password;

    public void signup() {
        String newUsername;
        String newPassword;
        List<String> users = readUsers();

        do {
            System.out.print("Enter username: ");
            newUsername = kb.nextLine();

            if(userExists(users, newUsername))
                System.out.println("\nUsername already exists.\n");
        } while(userExists(users, newUsername));

        do {
            System.out.print("Enter password: ");
            newPassword = kb.nextLine();

            if(newPassword.length() < 1)
                System.out.println("\nPassword is too short.\n");
        } while(newPassword.length() < 1);
        

        addUser(newUsername, newPassword);
        username = newUsername;
        password = newPassword;
    }

    public void login() {
        List<String> users = readUsers();
        String newUsername;
        String newPassword;

        do {
            System.out.print("Enter username: ");
            newUsername = kb.nextLine();

            if(!userExists(users, newUsername))
                System.out.println("\nUsername does not exist.\n");
        } while(!userExists(users, newUsername));

        do {
            System.out.print("Enter password: ");
            newPassword = kb.nextLine();

            if(!correctPassword(users, newPassword))
                System.out.println("\nIncorrect password.\n");
        } while(!correctPassword(users, newPassword));

        username = newUsername;
        password = newPassword;
    }

    private boolean correctPassword(List<String> users, String newPassword) {
        for(String user : users) {
            String password = user.split(":")[1];

            if(password.equals(newPassword))
                return true;
        }
        return false;
    }

    private void addUser(String newUsername, String newPassword) {
        File file = new File("users.txt");

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(newUsername + ":" + newPassword + "\n");
            fw.close();
        } catch(IOException e) {
            System.out.println("\nError.\n");
        }

        new File(newUsername).mkdir();
    }

    private boolean userExists(List<String> users, String newUsername) {
        for(String user : users) {
            String username = user.split(":")[0];

            if(username.equals(newUsername))
                return true;
        }  

        return false;
    }

    private List<String> readUsers() {
        List<String> users = new ArrayList<>();

        if(new File("users.txt").exists()) {
            try {
                Path filePath = Paths.get("users.txt");

                users = Files.readAllLines(filePath);
    
    
            } catch(IOException e) {
                System.out.println("\nError.\n");
            }
        }

        return users;
        
    }

}