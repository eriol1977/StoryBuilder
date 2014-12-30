package storybuilder;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.w3c.dom.Node;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandsForm extends AbstractForm
{

    private TableColumn nameCol;

    private TableColumn keywordCol;

    private TableColumn descriptionCol;

    public static void main(String[] args)
    {
        launch(args);
    }

    public CommandsForm()
    {
        super("Commands", "c_", "commands", 520, 600);
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        nameCol = getColumn("name", 100);
        nameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Command, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Command, String> t)
                    {
                        ((Command) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setName(t.getNewValue());
                    }
                }
        );

        keywordCol = getColumn("keyword", 100);
        keywordCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Command, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Command, String> t)
                    {
                        ((Command) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setKeyword(t.getNewValue());
                    }
                }
        );

        descriptionCol = getColumn("description", 200);
        descriptionCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Command, String>>()
                {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Command, String> t)
                    {
                        ((Command) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setDescription(t.getNewValue());
                    }
                }
        );

        final List<TableColumn> columns = new ArrayList<>(3);
        columns.add(nameCol);
        columns.add(keywordCol);
        columns.add(descriptionCol);
        return columns;
    }

    @Override
    protected HBox getAddNewElementBox()
    {
        final Label prefixLabel = new Label(prefix);
        prefixLabel.setAlignment(Pos.BOTTOM_CENTER);
        final Font font = new Font("Arial", 15);
        prefixLabel.setFont(font);

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(nameCol.getPrefWidth());

        final TextField addKeyword = new TextField();
        addKeyword.setMaxWidth(keywordCol.getPrefWidth());
        addKeyword.setPromptText("Keyword");

        final TextField addDescription = new TextField();
        addDescription.setMaxWidth(descriptionCol.getPrefWidth());
        addDescription.setPromptText("Description");

        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            data.add(new Command(
                    prefix + addName.getText(),
                    addKeyword.getText(),
                    addDescription.getText()));
            message.setText("Element " + prefix + addName.getText() + " added");
            addName.clear();
            addKeyword.clear();
            addDescription.clear();
        });

        final HBox hbAdd = new HBox();
        hbAdd.getChildren().addAll(prefixLabel, addName, addKeyword, addDescription, addButton);
        hbAdd.setSpacing(10);

        return hbAdd;
    }

    @Override
    protected IStoryElement parseElement(final Node node)
    {
        return new Command(node);
    }
}
