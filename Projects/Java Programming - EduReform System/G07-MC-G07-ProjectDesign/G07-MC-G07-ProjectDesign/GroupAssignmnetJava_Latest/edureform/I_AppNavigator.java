import javax.swing.JPanel;

/**
 * @file: I_AppNavigator.java
 * @creator: Rayme Ryan Anak Roxy
 * @tester: Nur Fatima Damia
 * @description: Core navigation interface managing mobile-frame panel routing and data storage hooks.
 */
public interface I_AppNavigator {
    
    // Core Navigation Methods
    void navigateTo(String screenName, JPanel targetPanel);
    void exitApplication();
    void showRewardsPanel();
    
    // Data Management Methods (Absorbed Role 5 duties)
    void saveUserScore(String name, int score, double percentage);
    void displayLeaderboard();
}