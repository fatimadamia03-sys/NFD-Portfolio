import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * // --- CONTRIBUTED BY: RAYME RYAN ANAK ROXY (106122) ---
 * @file: EduReformSystem.java
 * @description: Core application container. Manages multi-page routing via CardLayout,
 * separating login, primary dashboard hub, and profile configurations.
 */
public class EduReformSystem extends JFrame implements I_AppNavigator {

    private static final int WIDTH = 360;
    private static final int HEIGHT = 740;
    
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private final String recordFilePath = "records.txt";
    
     // User Session Data
    private String currentUserName = "";
    
    // Global Labels to display active profile info dynamically
    private JLabel hubProfileLabel;
    private JTextField settingsNameField;

    // --- CONTRIBUTED BY: MUHAMMAD MARWAN HAKIM BIN ABDUL MALIK (105252) ---
    // Quiz Engine and UI State
    // The quiz module is built from a quiz manager object that holds questions and score state,
    // a dedicated quiz panel for rendering the quiz screen, and widgets for displaying the
    // current question, answer options, feedback, and quiz navigation controls.

    // Quiz Engine and UI State
    private JPanel quizPanel;
    private QuizManagerLogic quizManager;
    private GamificationHub gamificationHub;
    private JLabel quizStageLabel;
    private JTextArea quizQuestionLabel;
    private JPanel quizOptionsPanel;
    private ButtonGroup quizOptionsGroup;
    private JLabel quizFeedbackLabel;
    private JButton quizActionButton;
    private JButton quizBackButton;

    private JPanel rewardsPanel;
    private JLabel rewardPointsLabel;
    private JLabel rewardLevelLabel;
    private JLabel rewardStreakLabel;
    private JLabel rewardBestTimeLabel; // NEW: Label for personal best
    private JPanel badgeListPanel;

    public EduReformSystem() {
        setTitle("EduReform 2026 - SDG 4");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        // We will initialize the hub later when the quiz starts!

        JPanel loginPanel = createLoginPanel();
        JPanel menuHubPanel = createMenuHubPanel();
        JPanel settingsPanel = createSettingsPanel();

        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(menuHubPanel, "MENU_HUB");
        mainContainer.add(settingsPanel, "SETTINGS");

        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN"); 
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(41, 128, 185)); 
        panel.setBorder(BorderFactory.createEmptyBorder(50, 25, 50, 25));

        JLabel logoLabel = new JLabel("🏛️", SwingConstants.CENTER);
        logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 70));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("EduReform 2026");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel promptLabel = new JLabel("Enter Name / Matric ID to unlock:");
        promptLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        promptLabel.setForeground(new Color(236, 240, 241));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(260, 40));
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        JButton btnLogin = new JButton("Access Portal ➔");
        btnLogin.setMaximumSize(new Dimension(260, 45));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(46, 204, 113)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addActionListener(e -> {
            String enteredText = inputField.getText().trim();
            if (enteredText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Valid student credentials required.", 
                        "Authentication Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                currentUserName = enteredText;
                hubProfileLabel.setText("Active User: " + currentUserName);
                settingsNameField.setText(currentUserName);
                
                // NEW: Initialize the Hub and load the specific user's data!
                gamificationHub = new GamificationHub();
                gamificationHub.loadProgress(currentUserName);
                
                cardLayout.show(mainContainer, "MENU_HUB");
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(logoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(promptLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(inputField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnLogin);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createMenuHubPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel("EduReform Hub");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hubProfileLabel = new JLabel("Active User: Anonymous");
        hubProfileLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        hubProfileLabel.setForeground(new Color(127, 140, 141));
        hubProfileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnEducation = createStyledButton("📚 Education Learning Module", new Color(46, 204, 113));
        JButton btnQuiz = createStyledButton("📝 Take EduQuiz", new Color(155, 89, 182));
        JButton btnLeaderboard = createStyledButton("🏆 Leaderboard / Rewards", new Color(241, 196, 15));
        JButton btnSettings = createStyledButton("⚙️ Account Settings", new Color(149, 165, 166));
        JButton btnExit = createStyledButton("❌ Exit App", new Color(231, 76, 60));

        btnEducation.addActionListener(e -> {
            EducationModule educationModule = new EducationModule(() -> cardLayout.show(mainContainer, "MENU_HUB"));
            navigateTo("EDUCATION_MODULE", educationModule);
        });

        btnQuiz.addActionListener(e -> {
            if (quizPanel == null) {
                quizPanel = createQuizPanel();
                mainContainer.add(quizPanel, "QUIZ_MODULE");
            }
            quizManager.resetQuiz();
            updateQuizPanelUI();
            cardLayout.show(mainContainer, "QUIZ_MODULE");
        });

        btnLeaderboard.addActionListener(e -> showRewardsPanel());
        btnSettings.addActionListener(e -> cardLayout.show(mainContainer, "SETTINGS"));
        btnExit.addActionListener(e -> exitApplication());

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(hubProfileLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        panel.add(btnEducation);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnQuiz);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnLeaderboard);
        panel.add(Box.createVerticalGlue());
        panel.add(btnSettings);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnExit);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94)); 
        panel.setBorder(BorderFactory.createEmptyBorder(40, 25, 40, 25));

        JLabel settingsTitle = new JLabel("Account Settings");
        settingsTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        settingsTitle.setForeground(Color.WHITE);
        settingsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel modificationPrompt = new JLabel("Update Name / Matric ID:");
        modificationPrompt.setFont(new Font("SansSerif", Font.PLAIN, 12));
        modificationPrompt.setForeground(new Color(189, 195, 199));
        modificationPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        settingsNameField = new JTextField();
        settingsNameField.setMaximumSize(new Dimension(260, 40));
        settingsNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        settingsNameField.setHorizontalAlignment(JTextField.CENTER);

        JButton btnSaveChanges = new JButton("Save Changes");
        btnSaveChanges.setMaximumSize(new Dimension(260, 45));
        btnSaveChanges.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSaveChanges.setBackground(new Color(46, 204, 113));
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setFocusPainted(false);
        btnSaveChanges.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnReturnHub = new JButton("⬅ Back to Dashboard");
        btnReturnHub.setMaximumSize(new Dimension(260, 45));
        btnReturnHub.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnReturnHub.setBackground(new Color(231, 76, 60));
        btnReturnHub.setForeground(Color.WHITE);
        btnReturnHub.setFocusPainted(false);
        btnReturnHub.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSaveChanges.addActionListener(e -> {
            String updatedText = settingsNameField.getText().trim();
            if (updatedText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Profile identity cannot be empty.", 
                        "Update Error", JOptionPane.ERROR_MESSAGE);
            } else {
                currentUserName = updatedText;
                hubProfileLabel.setText("Active User: " + currentUserName);
                JOptionPane.showMessageDialog(this, "Profile identity updated successfully.", 
                        "Profile Saved", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainContainer, "MENU_HUB");
            }
        });

        btnReturnHub.addActionListener(e -> cardLayout.show(mainContainer, "MENU_HUB"));

        panel.add(settingsTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));
        panel.add(modificationPrompt);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(settingsNameField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(btnSaveChanges);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnReturnHub);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(280, 45));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private JPanel createQuizPanel() {
        quizManager = new QuizManagerLogic();
        quizManager.myHub = this.gamificationHub;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("EduQuiz: 20 Questions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizStageLabel = new JLabel("", SwingConstants.CENTER);
        quizStageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        quizStageLabel.setForeground(new Color(127, 140, 141));
        quizStageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizQuestionLabel = new JTextArea();
        quizQuestionLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        quizQuestionLabel.setForeground(new Color(44, 62, 80));
        quizQuestionLabel.setLineWrap(true);
        quizQuestionLabel.setWrapStyleWord(true);
        quizQuestionLabel.setEditable(false);
        quizQuestionLabel.setOpaque(false);
        quizQuestionLabel.setFocusable(false);
        
        // FIX: Add margins to push the text toward the center so it matches your image
        quizQuestionLabel.setMargin(new Insets(10, 50, 10, 20)); 
        quizQuestionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizQuestionLabel.setMaximumSize(new Dimension(320, 100));

        // Create the inner panel for options (Left Aligned internally)
        quizOptionsPanel = new JPanel();
        quizOptionsPanel.setLayout(new BoxLayout(quizOptionsPanel, BoxLayout.Y_AXIS));
        quizOptionsPanel.setBackground(new Color(236, 240, 241));

        // FIX: Create an invisible wrapper panel to perfectly center the block of radio buttons!
        JPanel optionsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionsWrapper.setBackground(new Color(236, 240, 241));
        optionsWrapper.add(quizOptionsPanel);
        optionsWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizFeedbackLabel = new JLabel("Select one answer and press Next.", SwingConstants.CENTER);
        quizFeedbackLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        quizFeedbackLabel.setForeground(new Color(39, 174, 96));
        quizFeedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        quizActionButton = createStyledButton("Next Question", new Color(52, 152, 219));
        quizActionButton.setMaximumSize(new Dimension(220, 40));
        quizActionButton.addActionListener(e -> handleQuizAction());

        quizBackButton = createStyledButton("⬅ Back to Menu Hub", new Color(231, 76, 60));
        quizBackButton.setMaximumSize(new Dimension(220, 40));
        quizBackButton.addActionListener(e -> cardLayout.show(mainContainer, "MENU_HUB"));

        // Put all the pieces onto the screen
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(quizStageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25))); // Extra space before question
        panel.add(quizQuestionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add the Wrapper Panel instead of the raw options panel
        panel.add(optionsWrapper); 
        
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(quizFeedbackLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(quizActionButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(quizBackButton);
        panel.add(Box.createVerticalGlue());

        updateQuizPanelUI();
        return panel;
    }

    private void updateQuizPanelUI() {
        QuizQuestion currentQuestion = quizManager.getCurrentQuestion();
        if (currentQuestion == null) return;

        quizStageLabel.setText(String.format("Question %d of %d", quizManager.getCurrentIndex() + 1, quizManager.getTotalQuestions()));
        quizQuestionLabel.setText(currentQuestion.getPrompt());
        quizFeedbackLabel.setText("Select one answer and press Next.");
        quizFeedbackLabel.setForeground(new Color(39, 174, 96));

        quizOptionsPanel.removeAll();
        quizOptionsGroup = new ButtonGroup();
        
        for (String choice : currentQuestion.getOptions()) {
            // FIX: Shrunk the width slightly to fit the centered layout better
            String wrappedChoice = "<html><p style='width: 170px; margin: 0px; padding: 2px;'>" + choice + "</p></html>";
            
            JRadioButton radioButton = new JRadioButton(wrappedChoice);
            radioButton.setActionCommand(choice);
            radioButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
            radioButton.setBackground(new Color(236, 240, 241));
            radioButton.setFocusPainted(false); // Removes the ugly dotted square when clicked
            
            quizOptionsGroup.add(radioButton);
            quizOptionsPanel.add(radioButton);
            quizOptionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        if (quizManager.isLastQuestion()) {
            quizActionButton.setText("Submit Quiz");
        } else {
            quizActionButton.setText("Next Question");
        }

        quizManager.startQuestionTimer();
        quizOptionsPanel.revalidate();
        quizOptionsPanel.repaint();
    }

    private void handleQuizAction() {
        String selectedAnswer = getSelectedQuizAnswer();
        if (selectedAnswer == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer before continuing.", "Answer Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        QuizQuestion currentQuestion = quizManager.getCurrentQuestion();
        
        // The beginner submitAnswer automatically checks the timer and streak for us now!
        boolean correct = quizManager.submitAnswer(selectedAnswer);

        if (correct) {
            quizFeedbackLabel.setText("Correct! Keep going.");
            quizFeedbackLabel.setForeground(new Color(39, 174, 96));
        } else {
            quizFeedbackLabel.setText("Incorrect. Correct answer: " + currentQuestion.getCorrectAnswer());
            quizFeedbackLabel.setForeground(new Color(192, 57, 43));
        }

        if (quizManager.isLastQuestion()) {
            displayQuizResults();
        } else {
            quizManager.moveToNextQuestion();
            updateQuizPanelUI();
        }
    }

    public void showRewardsPanel() {
        if (rewardsPanel == null) {
            // Make sure the hub exists even if they haven't started a quiz yet
            if (gamificationHub == null) { gamificationHub = new GamificationHub(); }
            rewardsPanel = createRewardsPanel();
            mainContainer.add(rewardsPanel, "REWARDS");
        }
        refreshRewardsPanel();
        cardLayout.show(mainContainer, "REWARDS");
    }

    private JPanel createRewardsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🏅 Rewards & Badges", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rewardPointsLabel = new JLabel("Points: 0", SwingConstants.CENTER);
        rewardPointsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rewardPointsLabel.setForeground(new Color(39, 174, 96));
        rewardPointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rewardLevelLabel = new JLabel("Level: 1", SwingConstants.CENTER);
        rewardLevelLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rewardLevelLabel.setForeground(new Color(52, 152, 219));
        rewardLevelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rewardStreakLabel = new JLabel("Current Streak: 0", SwingConstants.CENTER);
        rewardStreakLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rewardStreakLabel.setForeground(new Color(142, 68, 173));
        rewardStreakLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // The new Best Time Label
        rewardBestTimeLabel = new JLabel("Best Time: N/A", SwingConstants.CENTER);
        rewardBestTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rewardBestTimeLabel.setForeground(new Color(230, 126, 34)); 
        rewardBestTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        badgeListPanel = new JPanel();
        badgeListPanel.setLayout(new BoxLayout(badgeListPanel, BoxLayout.Y_AXIS));
        badgeListPanel.setBackground(new Color(236, 240, 241));

        JScrollPane badgeScrollPane = new JScrollPane(badgeListPanel);
        badgeScrollPane.setPreferredSize(new Dimension(300, 180));
        badgeScrollPane.setBorder(BorderFactory.createTitledBorder("Badges"));

        JButton backButton = createStyledButton("⬅ Back to Dashboard", new Color(231, 76, 60));
        backButton.setMaximumSize(new Dimension(260, 45));
        backButton.addActionListener(e -> cardLayout.show(mainContainer, "MENU_HUB"));

        JButton saveProgressButton = createStyledButton("💾 Save Progress", new Color(52, 152, 219));
        saveProgressButton.setMaximumSize(new Dimension(260, 45));
        
        saveProgressButton.addActionListener(e -> {
            try {
                gamificationHub.saveProgress();
                JOptionPane.showMessageDialog(this, "Progress saved.", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Adding everything to the screen safely
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(rewardPointsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(rewardLevelLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(rewardStreakLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(rewardBestTimeLabel); 
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(badgeScrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(saveProgressButton);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void refreshRewardsPanel() {
        rewardPointsLabel.setText("Points: " + gamificationHub.totalPoints);
        rewardLevelLabel.setText("Level: " + gamificationHub.level);
        rewardStreakLabel.setText("Current Streak: " + gamificationHub.streak);
        
        if (gamificationHub.bestQuizTime == 9999) {
            rewardBestTimeLabel.setText("Best Quiz Time: No record yet");
        } else {
            rewardBestTimeLabel.setText("Best Quiz Time: " + gamificationHub.bestQuizTime + " seconds");
        }

        badgeListPanel.removeAll();
        boolean anyEarned = false;

        for (int i = 0; i < gamificationHub.allPossibleBadges.length; i++) {
            Badge badge = gamificationHub.allPossibleBadges[i];
            if (badge == null) continue;

            boolean earned = false;
            for (int j = 0; j < gamificationHub.badgeCount; j++) {
                if (gamificationHub.myEarnedBadges[j].id.equals(badge.id)) {
                    earned = true;
                    anyEarned = true;
                    break;
                }
            }

            JPanel badgeCard = new JPanel();
            badgeCard.setLayout(new BoxLayout(badgeCard, BoxLayout.X_AXIS));
            badgeCard.setBackground(earned ? new Color(237, 247, 237) : new Color(250, 250, 250));
            badgeCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)));

            ImageIcon icon = loadBadgeIcon(badge.image);
            JLabel iconLabel = (icon != null) ? new JLabel(icon) : new JLabel("🏆");
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(badge.name);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setForeground(new Color(44, 62, 80));

            JLabel hintLabel = new JLabel(badge.hint);
            hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
            hintLabel.setForeground(new Color(127, 140, 141));

            JLabel statusLabel = new JLabel(earned ? "Earned" : "Locked");
            statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            statusLabel.setForeground(earned ? new Color(39, 174, 96) : new Color(149, 165, 166));

            textPanel.add(nameLabel);
            textPanel.add(hintLabel); 
            textPanel.add(statusLabel);

            badgeCard.add(iconLabel);
            badgeCard.add(textPanel);
            
            badgeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); 
            badgeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            badgeListPanel.add(badgeCard);
            badgeListPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        if (!anyEarned) {
            JLabel noneLabel = new JLabel("No badges earned yet. Keep answering questions!");
            noneLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            badgeListPanel.add(noneLabel);
        }

        badgeListPanel.revalidate();
        badgeListPanel.repaint();
    }

    private ImageIcon loadBadgeIcon(String iconFile) {
        if (iconFile == null || iconFile.isEmpty()) return null;
        File imageFile = new File("images" + File.separator + iconFile);
        if (!imageFile.exists()) return null;

        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
        Image image = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private String getSelectedQuizAnswer() {
        if (quizOptionsGroup == null) return null;
        ButtonModel selectedModel = quizOptionsGroup.getSelection();
        return selectedModel == null ? null : selectedModel.getActionCommand();
    }

    private void displayQuizResults() {
        // Run our new beginner finish method
        quizManager.finishQuizSession();

        int score = quizManager.getScore();
        int total = quizManager.getTotalQuestions();
        double percentage = quizManager.getPercentage();
        String badgeStr = QuizEvaluator.getBadge(percentage);
        String grade = QuizEvaluator.getGrade(percentage);

        // Export the score and QuizEvaluator data!
        saveUserScore(currentUserName, score, percentage);

        // Build string of earned badges to show user
        String earnedBadges = "";
        if (gamificationHub.badgeCount == 0) {
            earnedBadges = "None yet";
        } else {
            for (int i = 0; i < gamificationHub.badgeCount; i++) {
                earnedBadges += gamificationHub.myEarnedBadges[i].name;
                if (i < gamificationHub.badgeCount - 1) earnedBadges += ", ";
            }
        }

        // NEW: Let's figure out the scoring scale text based on their percentage
        String scaleText = "";
        if (percentage >= 90) {
            scaleText = "90-100%: Excellent mastery";
        } else if (percentage >= 80) {
            scaleText = "80-89%: Good mastery";
        } else if (percentage >= 70) {
            scaleText = "70-79%: Average mastery";
        } else {
            scaleText = "Below 70%: Needs improvement";
        }

        // FIX: Rebuild the message to perfectly match your 2nd picture!
        String message = "Score: " + score + "/" + total + "\n" +
                         "Percentage: " + percentage + "%\n" +
                         "Grade: " + grade + "\n" +
                         "Badge: " + badgeStr + "\n" +
                         "Points: " + gamificationHub.totalPoints + "\n" +
                         "Level: " + gamificationHub.level + "\n" +
                         "Earned Badges: " + earnedBadges + "\n\n" +
                         "Scoring Scale: " + scaleText;

        JOptionPane.showMessageDialog(this, message, "Quiz Complete!", JOptionPane.INFORMATION_MESSAGE);

        quizManager.resetQuiz();
        updateQuizPanelUI();
    }

    @Override
    public void navigateTo(String screenName, JPanel targetPanel) {
        mainContainer.add(targetPanel, screenName);
        cardLayout.show(mainContainer, screenName);
    }

    @Override
    public void exitApplication() {
        System.exit(0);
    }

    @Override
    public void saveUserScore(String name, int score, double percentage) {
        // 1. Get the feedback strings from your QuizEvaluator file
        String feedback = QuizEvaluator.getBadge(percentage);
        String grade = QuizEvaluator.getGrade(percentage);
        
        try {
            // 2. Open a new text file. 
            // The "true" parameter tells Java to append to the bottom instead of overwriting!
            FileWriter myWriter = new FileWriter("quiz_history.txt", true); 
            
            // 3. Write the data in a clean format
            myWriter.write("Student: " + name + " | Score: " + score + " | Grade: " + grade + " | Feedback: " + feedback + "\n");
            
            // 4. Close the file
            myWriter.close();
            
        } catch (Exception e) {
            System.out.println("Error exporting score.");
        }
    }

    @Override
    public void displayLeaderboard() {
        // Not implemented in GUI
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EduReformSystem engineInstance = new EduReformSystem();
            engineInstance.setVisible(true);
        });
    }
}