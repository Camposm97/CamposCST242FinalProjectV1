
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The purpose of the this class is to provide a method that 
 * enables you to create an alert in a few of lines 
 */
public class CustomAlert {
    //Instance Variable Section
    private Alert alert;
    
    //Default CustomAlert Constructor
    public CustomAlert() {
    }

    public void Create(AlertType alertType, String title, String headerText,
            String contentText)
    {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}