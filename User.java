import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class User {

    private Scanner kb = new Scanner(System.in);
    String username;
    String password;

    /**
     * Prompts user to signup with username and password 
     */
    public void signup() {
        Map<String, String> users = readUsers();
        String newUsername;
        String newPassword;
        int attempts = 0;

        // gets username until a unique username is inputted
        do {
            System.out.print("Enter username: ");
            newUsername = kb.nextLine();
            attempts++;

            if(newUsername.length() <= 1) {
                System.out.println("\nUsername is too short.\n");
            }
            if(users.containsKey(newUsername)) {
                System.out.println("\nUsername already exists.\n");
            }
        } while((users.containsKey(newUsername) || newUsername.length() <= 1) && attempts < 3);

        if(attempts == 3) {
            System.out.println("Too many attempts.\n");
            return;
        }
        
        attempts = 0;

        // gets password that is longer than 1 character
        do {
            System.out.print("Enter password: ");
            newPassword = kb.nextLine();
            attempts++;

            if(newPassword.length() <= 1) {
                System.out.println("\nPassword is too short.\n");
            }
        } while(newPassword.length() <= 1 && attempts < 3);

        if(attempts == 3) {
            System.out.println("Too many attempts.\n");
            return;
        }

        addUser(newUsername, newPassword);
        username = newUsername;
        password = newPassword;
    }

    /**
     * Prompts user to login with username and password
     */
    public void login() {
        Map<String,String> users = readUsers();
        String newUsername;
        String newPassword;
        int attempts = 0;

        // gets username that must exist, 3 attempts are given to user
        do {
            System.out.print("Enter username: ");
            newUsername = kb.nextLine();
            attempts++;

            if(!users.containsKey(newUsername)) {
                System.out.println("\nUsername does not exist.\n");
            }
        } while(!users.containsKey(newUsername));

        if(attempts == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        attempts = 0;

        // gets password that must match username password, 3 attempts are given to user
        do {
            System.out.print("Enter password: ");
            newPassword = kb.nextLine();
            attempts++;

            if(!users.get(newUsername).equals(newPassword)) {
                System.out.println("\nIncorrect password.\n");
            }
        } while(!users.get(newUsername).equals(newPassword));

        if(attempts == 3) {
            System.out.println("\nToo many attempts.");
            return;
        }

        username = newUsername;
        password = newPassword;
    }

    /**
     * Adds a new user to the file and creates a directory for the user
     * @param newUsername
     * @param newPassword
     */
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

    /**
     * Reads all users that exist
     * @return
     */
    private Map<String, String> readUsers() {
        Map<String, String> users = new HashMap<>();

        if(new File("users.txt").exists()) {
            try {
                Path filePath = Paths.get("users.txt");
                List<String> lines = Files.readAllLines(filePath);
    
                for(String user : lines) {
                    String[] split = user.split(":");
                    users.put(split[0], split[1]);
                }
            } catch(IOException e) {
                System.out.println("\nError.\n");
            }
        }

        return users;
    }

}
