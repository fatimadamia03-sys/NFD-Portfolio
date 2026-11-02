/* 
    --- Contributed by DARRYIELL KARLLEN (103793) ---
    Description: A gamification system to reward users with points and badges based on their performance and engagement.
    - Users earn points for correct answers, speed, and streaks.
    - Badges are awarded for specific achievements (e.g., perfect score, quick answers).
    - Progress is persisted across sessions
*/

// Tested by MUHAMMAD MARWAN HAKIM BIN ABDUL MALIK (105252)

import java.io.FileWriter;
import java.time.LocalDate;
import java.io.File;
import java.util.Scanner;

// 1. Our Badge blueprint
class Badge {
    public String id;
    public String name;
    public int points;
    public String image;
    public String hint; //  Added hint variable

    public Badge(String id, String name, int points, String image, String hint) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.image = image;
        this.hint = hint;
    }
}

public class GamificationHub {
    
    // 2. The Master List of all possible badges in the game
    public Badge[] allPossibleBadges = new Badge[4]; 
    
    // 3. The user's inventory (Now holding Badge objects)
    public Badge[] myEarnedBadges = new Badge[100];
    public int badgeCount = 0; 

    public int totalPoints = 0;
    public int streak = 0;
    public int level = 1;
    public String lastActivityDate;

    public String currentMatricNo = "Anonymous";
    
    //  Variable to track the fastest answer (starts at 999 so the first answer always beats it)
    public int bestQuizTime = 9999; // tracking the overall quiz time now!

    public GamificationHub() {
        lastActivityDate = LocalDate.now().toString(); 
        
        //  We added the hint text to the end of every badge!
        allPossibleBadges[0] = new Badge("SPEED_DEMON", "⚡ Speed Demon", 30, "speed_demon.png", "Answer under 30 seconds");
        allPossibleBadges[1] = new Badge("QUICK_THINKER", "💡 Quick Thinker", 20, "quick_thinker.png", "Answer under 60 seconds");
        allPossibleBadges[2] = new Badge("STRONG_STREAK", "🔥 Strong Streak", 50, "strong_streak.png", "Get 5 correct in a row");
        allPossibleBadges[3] = new Badge("PERFECT_SCORE", "🎯 Perfect Score", 100, "perfect_score.png", "Get a 100% on a quiz");
    }

    // 5. The Award Method
    public void awardBadge(String targetId) {
        // First, check if they already have it
        for (int i = 0; i < badgeCount; i++) {
            if (myEarnedBadges[i].id.equals(targetId)) {
                return; // Stop here, they already earned it!
            }
        }

        // Next, find the badge in the master list
        for (int i = 0; i < allPossibleBadges.length; i++) {
            if (allPossibleBadges[i] != null && allPossibleBadges[i].id.equals(targetId)) {
                
                // We found it! Add the whole object to the user's inventory
                myEarnedBadges[badgeCount] = allPossibleBadges[i];
                badgeCount++;
                
                // Automatically give them the points attached to the badge
                totalPoints = totalPoints + allPossibleBadges[i].points;
                
                System.out.println("Congratulations! You earned: " + allPossibleBadges[i].name);
                checkLevelUp();
                return; 
            }
        }
    }

    public void checkTime(int timeInSeconds) {
        // We only check for badges here! No time tracking!
        if (timeInSeconds <= 30) {
            awardBadge("SPEED_DEMON");
        } else if (timeInSeconds <= 60) {
            awardBadge("QUICK_THINKER");
        }
    }

    public void answerQuestion(boolean isRight) {
        if (isRight) {
            streak++;
            if (streak == 5) {
                awardBadge("STRONG_STREAK");
            }
        } else {
            streak = 0;
        }
    }

    public void finishQuiz(int score, int totalQuestions, int totalTimeSeconds) {
        if (score == totalQuestions) {
            awardBadge("PERFECT_SCORE");
        }
        
        if (totalTimeSeconds < bestQuizTime) {
            bestQuizTime = totalTimeSeconds;
        }
        
        checkLevelUp();
    }

    public void checkLevelUp() {
        int expectedLevel = 1 + (totalPoints / 100);
        if (expectedLevel > level) {
            level = expectedLevel;
            System.out.println("LEVEL UP! You are now level " + level);
        }
    }

    public void printEverything() {
        System.out.println("\n--- YOUR STATS ---");
        System.out.println("Last Active: " + lastActivityDate);
        System.out.println("Points: " + totalPoints);
        System.out.println("Level: " + level);
        System.out.println("Streak: " + streak);
        System.out.println("Badges Earned: " + badgeCount);
        
        for (int i = 0; i < badgeCount; i++) {
            Badge b = myEarnedBadges[i];
            System.out.println("- " + b.name + " (Image: " + b.image + ")");
        }
        System.out.println("------------------\n");
    }

    // NEW: Loads data specifically for the Matric No
    public void loadProgress(String matricNo) {
        this.currentMatricNo = matricNo;
        
        // Reset everything to 0 first (in case it's a brand new user)
        totalPoints = 0;
        level = 1;
        streak = 0;
        bestQuizTime = 9999;
        badgeCount = 0;
        myEarnedBadges = new Badge[100]; 

        try {
            File file = new File("records.txt");
            if (!file.exists()) return; // If file doesn't exist, just start fresh

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Check if this line belongs to our user!
                if (parts.length >= 5 && parts[0].equals(matricNo)) {
                    totalPoints = Integer.parseInt(parts[1]);
                    level = Integer.parseInt(parts[2]);
                    streak = Integer.parseInt(parts[3]);
                    bestQuizTime = Integer.parseInt(parts[4]);

                    // Load badges if they have any
                    if (parts.length >= 6 && !parts[5].isEmpty()) {
                        String[] savedBadgeIds = parts[5].split(";");
                        for (int i = 0; i < savedBadgeIds.length; i++) {
                            for (int j = 0; j < allPossibleBadges.length; j++) {
                                if (allPossibleBadges[j] != null && allPossibleBadges[j].id.equals(savedBadgeIds[i])) {
                                    myEarnedBadges[badgeCount] = allPossibleBadges[j];
                                    badgeCount++;
                                }
                            }
                        }
                    }
                    break; // We found the user, stop searching the file!
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Could not load user data.");
        }
    }

    // NEW: Rewrites the records.txt file and updates ONLY this user's line
    public void saveProgress() throws Exception {
        String[] allLines = new String[1000];
        int lineCount = 0;
        boolean foundUser = false;

        // Build the new data string (separating badges with a semi-colon)
        String earnedBadges = "";
        for (int i = 0; i < badgeCount; i++) {
            earnedBadges += myEarnedBadges[i].id;
            if (i < badgeCount - 1) earnedBadges += ";";
        }
        String myNewData = currentMatricNo + "," + totalPoints + "," + level + "," + streak + "," + bestQuizTime + "," + earnedBadges;

        // Read all existing users so we don't delete them
        File file = new File("records.txt");
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                
                if (parts.length > 0 && parts[0].equals(currentMatricNo)) {
                    allLines[lineCount] = myNewData; // Replace their old data with new data
                    foundUser = true;
                } else {
                    allLines[lineCount] = line; // Copy other users exactly as they are
                }
                lineCount++;
            }
            scanner.close();
        }

        // If the user wasn't in the file, add them to the bottom
        if (foundUser == false) {
            allLines[lineCount] = myNewData;
            lineCount++;
        }

        // Rewrite the entire file with the updated lines
        FileWriter myWriter = new FileWriter("records.txt");
        for (int i = 0; i < lineCount; i++) {
            myWriter.write(allLines[i] + "\n");
        }
        myWriter.close();
    }

    // --- MAIN METHOD TO TEST THE CODE ---
    public static void main(String[] args) {
        GamificationHub myGame = new GamificationHub();
        
        System.out.println("Starting the game simulation...\n");

        // Simulate answering 5 questions correctly
        myGame.answerQuestion(true);
        myGame.answerQuestion(true);
        myGame.answerQuestion(true);
        myGame.answerQuestion(true);
        myGame.answerQuestion(true); // Should trigger the FIRE badge!

        // Simulate answering a question really fast (under 30 seconds)
        myGame.checkTime(25); // Should trigger the SPEED badge!
        
        // Simulate finishing a 10-question quiz perfectly
        myGame.finishQuiz(10, 10, 120); // Should trigger the PERFECT score badge!

        // Print out the final results
        myGame.printEverything();

        // Try saving it to a file
        try {
            myGame.saveProgress();
        } catch (Exception e) {
            System.out.println("Oops! Something went wrong saving the file.");
        }
    }
}