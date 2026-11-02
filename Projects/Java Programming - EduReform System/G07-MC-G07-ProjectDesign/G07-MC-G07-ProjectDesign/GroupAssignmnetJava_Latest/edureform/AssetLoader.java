
import javax.swing.ImageIcon;

/**
 * Contributed by: Nur Fatima Damia binti Mohammad Yazis (107468)
 * Role: Interface for loading text and image assets file.
 */
public interface AssetLoader {
    // Method 1: text loader - Takes a page number and promises to return the text for that page.
    // "throws ContentLoadException" - it will warn the program if a page doesn't exist.
    String loadText(int pageNumber) throws ContentLoadException;
    
   //  Method 2: Overloaded text loader - takes an extra "boolean" rule.
   // This version can return a shorter preview of the text if "shortVersion" is true.
    String loadText(int pageNumber, boolean shortVersion) throws ContentLoadException;
    
    //  Method 3: Image loader - // Takes the folder path of a picture and returns it as an ImageIcon that can be displayed on the screen.
    ImageIcon displayImage(String imagePath);
}