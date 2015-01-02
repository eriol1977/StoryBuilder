package storybuilder.preferences.view;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import storybuilder.main.AbstractView;

/**
 *
 * @author Francesco Bertolino
 */
public class PreferencesView extends AbstractView
{
    
    public PreferencesView()
    {
        addTitle("Preferences");
        
        final TextField textField = addLabeledTextInput("Directory");
        textField.setMinWidth(500);
        textField.setText(cache.getPreferences().getDirectoryPath());
        textField.setDisable(true);
        textField.setEditable(false);
        
        final Button button = addButton("Choose another");
        button.setOnAction((final ActionEvent e) -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            final File selectedDirectory = directoryChooser.showDialog(mwc.getStage());
            if (selectedDirectory != null) {
                cache.getPreferences().setDirectoryPath(selectedDirectory.getAbsolutePath());
                mwc.updateStatusBarMessage(selectedDirectory.getAbsolutePath() + " chosen");
                textField.setText(selectedDirectory.getAbsolutePath());
            }
        });
        
    }
}
