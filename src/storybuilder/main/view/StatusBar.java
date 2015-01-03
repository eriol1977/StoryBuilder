package storybuilder.main.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Francesco Bertolino
 */
public class StatusBar extends HBox
{

    private final Text message;

    StatusBar()
    {
        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(5, 5, 5, 5));
        setStyle("-fx-border-color: #CCCCCC;");
        message = new Text("Story Builder");
        getChildren().add(message);
    }

    void setMessage(final String value)
    {
        message.setText(value);
    }

}
