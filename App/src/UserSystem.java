import java.util.*;

public class UserSystem {

    // username -> userId
    private HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt frequency
    private HashMap<String, Integer> attempts = new HashMap<>();

    private int userIdCounter = 1;

    // Check username availability
    public boolean checkAvailability(String username) {
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);
        return !users.containsKey(username);
    }

    // Register user
    public boolean register(String username) {
        if (!checkAvailability(username)) {
            return false;
        }
        users.put(username, userIdCounter++);
        return true;
    }

    // Suggest alternative usernames
    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String suggestion = username + i;

            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String alt = username.replace("_", ".");
        if (!users.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {

        String most = null;
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

    // Main method to run program
    public static void main(String[] args) {

        UserSystem system = new UserSystem();

        // Pre-register users
        system.register("john_doe");
        system.register("admin");

        // Availability check
        System.out.println("john_doe → " + system.checkAvailability("john_doe"));
        System.out.println("jane_smith → " + system.checkAvailability("jane_smith"));

        // Suggestions
        System.out.println("Suggestions: " + system.suggestAlternatives("john_doe"));

        // Simulate many attempts
        for (int i = 0; i < 10543; i++) {
            system.checkAvailability("admin");
        }

        System.out.println("Most attempted: " + system.getMostAttempted());
    }
}