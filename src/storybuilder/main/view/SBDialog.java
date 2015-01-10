package storybuilder.main.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Francesco Bertolino
 */
public class SBDialog extends Stage
{

    private final VBox dialogVbox;

    public SBDialog()
    {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(MainWindowController.getInstance().getStage());
        dialogVbox = new VBox(20);
        dialogVbox.setSpacing(10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        Scene dialogScene = new Scene(dialogVbox, 700, 110);
        setScene(dialogScene);
    }

    public void add(final Node node)
    {
        dialogVbox.getChildren().add(node);
    }

    public void replaceLastNode(final Node node)
    {
        dialogVbox.getChildren().remove(dialogVbox.getChildren().size() - 1);
        add(node);
    }
}
