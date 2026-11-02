
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contributed by: Nur Fatima Damia binti Mohammad Yazis (107468)
 * Description: 10-page slider file
 */

public class EducationModule extends JPanel implements AssetLoader {    //using "extends JPanel" to make a UI container that holds other elements.

    private int currentPage = 0; //user looking at current page number, starting at 0 (first page)
    private final int TOTAL_PAGES = 10; // Total number of pages in the module
    private final Runnable onComplete; // Handles returning to the main menu after finishing the module
    
    // GUI-UI components (visual things we see on the screen) - using Swing components
    private JLabel titleLabel; // Title at the top of the module
    private JTextArea contentArea; // Area to display the educational text content
    private JLabel imageLabel; // Label to display images 
    private JButton nextButton; // Button to go to the next page
    private JButton prevButton; // Button to go back to the previous page

    // array holding the text content for each page, indexed from 0 to 9 (10 pages total)
    private final String[] pageContents = {
        "Page 1\n\nWelcome to the Syllabus Studio.\nToday we look at how curriculum design impacts the quality of education.",
        "Page 2\n\nThe Core Issue.\nOverloaded syllabi frequently lead to chronic sleep deprivation among university students.",
        "Page 3\n\nPhysical Impact.\nLack of sleep diminishes cognitive function and weakens the immune system.",
        "Page 4\n\nGroup Dynamics.\nUnstructured group work can cause unequal workload distribution, increasing anxiety.",
        "Page 5\n\nThe Solution: Phased Deadlines.\nBreaking large projects into smaller, graded milestones reduces cramming.",
        "Page 6\n\nActive Learning.\nMoving away from 3-hour lectures to interactive, think-aloud problem solving improves retention.",
        "Page 7\n\nHealthy Infrastructure.\nFaculty labs should have ergonomic seating and natural lighting to boost mood.",
        "Page 8\n\nAssessment Reform.\nReducing the weight of final exams in favor of continuous assessment lowers stress spikes.",
        "Page 9\n\nPeer Support Systems.\nIntegrating mandatory mental health check-ins within the academic framework.",
        "Page 10\n\nConclusion.\nA healthy syllabus creates healthy students. You are now ready for the EduQuiz!"
    };

    //Array holding the exact file names for the images for all 10 pages
    private final String[] imageFiles = {
        "page1.jpg",
        "page2.jpg",
        "page3.png",
        "page4.jpg",
        "page5.png",
        "page6.jpg",
        "page7.jpg",
        "page8.png",
        "page9.jpg",
        "page10.png"
    };

    // Constructor: runs exactly once when the EducationModule is first created
    public EducationModule(Runnable onComplete) {
        this.onComplete = onComplete; //Save the "go back to menu" command for later
        
        // Set up the layout and styling of the panel - inheritance from JPanel 
        setLayout(new BorderLayout(10, 10)); // Arranges items Top, Center, and Bottom
        setBackground(new Color(245, 247, 250)); // Sets a light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adds padding inside the window

        // Set up the Title
        titleLabel = new JLabel("📚 The Syllabus Studio", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22)); // Make text big and bold
        titleLabel.setForeground(new Color(44, 62, 80)); // Dark blue text color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Add space below title

        // Set up the content area
        contentArea = new JTextArea();
        contentArea.setWrapStyleWord(true); // Prevent cutting words in half at the end of a line
        contentArea.setLineWrap(true); // Make text drop to the next line when it hits the edge
        contentArea.setEditable(false); // Stop users from typing in the text box
        contentArea.setOpaque(false); // Make the text background transparent
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contentArea.setMargin(new Insets(10, 10, 10, 10)); // Inner padding

        // Set up the image area
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image horizontally
        imageLabel.setPreferredSize(new Dimension(300, 150)); // Set a fixed box size for the image to fit into

        // Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Put buttons in a straight row
        buttonPanel.setOpaque(false); // Transparent background for button area
        
        // Create styled buttons using a helper method
        prevButton = createStyledButton("⬅ Previous", new Color(149, 165, 166)); // Gray button
        nextButton = createStyledButton("Next ➔", new Color(46, 204, 113)); // Green button
        
        buttonPanel.add(prevButton); // Add Previous button to screen
        buttonPanel.add(nextButton); // Add Next button to screen

        // Put everything together in the main panel
        add(titleLabel, BorderLayout.NORTH); // Title at the top
        
        JPanel centerPanel = new JPanel(new BorderLayout()); //Group the image and text together
        centerPanel.setOpaque(false);
        centerPanel.add(imageLabel, BorderLayout.NORTH); // Image goes above the text
        centerPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER); // Text goes below image
        add(centerPanel, BorderLayout.CENTER); // Put this group in the middle of the screen
        
        add(buttonPanel, BorderLayout.SOUTH); // Buttons go at the bottom

        // button actions - what happens when you click them
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If user are not on the last page yet...
                if (currentPage < TOTAL_PAGES - 1) {
                    currentPage++; // Move to the next page number
                    updatePage(); // Refresh the screen with the new content
                } else {
                    // If user are on the last page, show a popup message
                    JOptionPane.showMessageDialog(null, "Module Complete! Returning to Menu Hub so you can take the Quiz.");
                    if (onComplete != null) {
                        onComplete.run(); // Triggers the return to the main menu
                    }
                }
            }
        });

        // Previous button action
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only go back if user are not on the very first page
                if (currentPage > 0) {
                    currentPage--; // Move to the previous page number
                    updatePage(); // Refresh the screen
                }
            }
        });

        // Load the first page
        updatePage();
    }

    // Helper method to make our buttons look nice without repeating the same code twice
    private JButton createStyledButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE); //White text
        button.setBackground(bg); // Custom background color
        button.setFocusPainted(false); //// Removes dotted line when clicked
        return button;
    }

    /**
     * This method refreshes the screen text and image every time a button is clicked.
     */
    private void updatePage() {
        try {
            // Get the text for the current page and put it on screen
            String text = loadText(currentPage);
            contentArea.setText(text);
            
            // Build the folder path to find the right image
            String imagePath = "images/" + imageFiles[currentPage];
            
            // Try to load the image file using our interface method
            ImageIcon icon = displayImage(imagePath);
            
            // If the image was found, resize it to fit perfectly
            if (icon != null) {
                // SCALE_SMOOTH makes sure the image doesn't look pixelated when resized
                Image scaledImg = icon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
                imageLabel.setText(""); // Clear any missing text
            } else {
                // If image is missing, show an error message instead
                imageLabel.setIcon(null);
                imageLabel.setText("[ Missing: " + imagePath + " ]");
            }
            
           // Update buttons
            prevButton.setEnabled(currentPage > 0);
            nextButton.setText(currentPage == TOTAL_PAGES - 1 ? "Finish Module" : "Next ➔");
            
        } catch (ContentLoadException e) {
            // If something is wrong, show the error on the screen
            contentArea.setText("Error loading content: " + e.getMessage());
        }
    }

    // --- INTERFACE IMPLEMENTATIONS (OVERRIDING) ---

    // Returns the exact text block for a specific page number
    @Override
    public String loadText(int pageNumber) throws ContentLoadException {
        // Error checking: make sure the page number actually exists
        if (pageNumber < 0 || pageNumber >= TOTAL_PAGES) {
            throw new ContentLoadException("Invalid page requested: " + pageNumber);
        }
        return pageContents[pageNumber];
    }

    // An overloaded version of loadText that can cut the text short if needed
    @Override
    public String loadText(int pageNumber, boolean shortVersion) throws ContentLoadException {
        String fullText = loadText(pageNumber);
        // If shortVersion is true, cut the text after 50 characters
        if (shortVersion && fullText.length() > 50) {
            return fullText.substring(0, 50) + "...";
        }
        return fullText;
    }

    // Looks inside the project folders to find the requested image file and returns it as an ImageIcon
    @Override
    public ImageIcon displayImage(String imagePath) {
        java.net.URL imgURL = getClass().getResource(imagePath);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
}
