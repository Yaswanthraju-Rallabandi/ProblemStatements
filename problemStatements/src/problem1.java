import java.util.*;

class UsernameChecker {

    // username -> userId
    private HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt count
    private HashMap<String, Integer> attempts = new HashMap<>();


    // Check username availability
    public boolean checkAvailability(String username) {

        // track frequency
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }


    // Register new user
    public void registerUser(String username, int userId) {

        if (checkAvailability(username)) {
            users.put(username, userId);
            System.out.println(username + " registered successfully.");
        } else {
            System.out.println(username + " is already taken.");
        }
    }


    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }


    // Get most attempted username
    public String getMostAttempted() {

        String most = "";
        int max = 0;

        for (String name : attempts.keySet()) {
            int count = attempts.get(name);

            if (count > max) {
                max = count;
                most = name;
            }
        }

        return most + " (" + max + " attempts)";
    }
}


public class problem1 {

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        // Existing users
        system.registerUser("john_doe", 101);
        system.registerUser("admin", 102);

        System.out.println("\nCheck Username Availability");

        System.out.println("john_doe → " + system.checkAvailability("john_doe"));
        System.out.println("jane_smith → " + system.checkAvailability("jane_smith"));

        System.out.println("\nSuggestions for john_doe:");
        System.out.println(system.suggestAlternatives("john_doe"));

        // simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        System.out.println("\nMost Attempted Username:");
        System.out.println(system.getMostAttempted());
    }
}