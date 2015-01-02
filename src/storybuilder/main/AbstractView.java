package storybuilder.main;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class AbstractView extends VBox
{

    protected final Cache cache;

    protected MainWindowController mwc;

    public AbstractView()
    {
        setSpacing(15);
        setPadding(new Insets(20, 20, 20, 20));

        cache = Cache.getInstance();
        mwc = MainWindowController.getInstance();
    }

    protected void add(final Node node)
    {
        getChildren().add(node);
    }

    protected void addTitle(final String title)
    {
        final Label label = new Label(title);
        label.setFont(new Font("Arial", 20));
        add(label);
        add(new Separator());
    }

    protected TextField addLabeledTextInput(final String label)
    {
        final Label labelField = new Label(label + ":");
        labelField.setFont(new Font("Arial", 18));
        final TextField textField = new TextField();
        textField.setPromptText(label);
        final HBox hBox = new HBox(labelField, textField);
        hBox.setSpacing(15);
        add(hBox);
        return textField;
    }

    protected Button addButton(final String label)
    {
        final Button button = new Button(label);
        button.setFont(new Font("Arial", 15));
        add(button);
        return button;
    }
}
