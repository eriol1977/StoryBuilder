package storybuilder.command.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import storybuilder.command.model.Command;
import storybuilder.main.IStoryElement;
import storybuilder.main.AbstractView;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandsView extends AbstractView
{

    private final ObservableList<IStoryElement> data = FXCollections.observableArrayList();

    public CommandsView()
    {
        addTitle("Commands");

        final HBox layout = new HBox(15);

        final TableView table = new TableView();
        table.setMaxWidth(132);
        layout.getChildren().add(table);

        data.add(new Command("c_up", "alzo", "Mi alzo"));
        data.add(new Command("c_lay_still", "resto", "Resto sdraiato"));
        data.add(new Command("c_run", "corro", "Corro"));
        data.add(new Command("c_walk", "cammino", "Cammino"));
        data.add(new Command("c_get", "prendo", "Prendo"));
        table.setItems(data);
        table.getColumns().addAll(getColumn("name", 100), getDeleteColumn());
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (table.getSelectionModel().getSelectedItem() != null) {
                if (layout.getChildren().size() > 1) {
                    layout.getChildren().remove(1);
                }
                layout.getChildren().add(new CommandDetailView((Command) table.getSelectionModel().getSelectedItem(), this));
            }
        });

        final Button newButton = addButton("New");
        newButton.setOnAction((ActionEvent event) -> {
            if (layout.getChildren().size() > 1) {
                layout.getChildren().remove(1);
            }
            layout.getChildren().add(new CommandDetailView(new Command("", "", ""), this));
        });

        add(layout);
    }

    protected TableColumn getColumn(final String fieldName, final double minWidth)
    {
        final TableColumn column = new TableColumn(fieldName.toUpperCase());
        column.setMinWidth(minWidth);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }

    private TableColumn getDeleteColumn()
    {
        TableColumn deleteCol = new TableColumn<>("Del");
        deleteCol.setSortable(false);
        deleteCol.setMaxWidth(30);
        deleteCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Object, Boolean>, ObservableValue<Boolean>>()
                {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Object, Boolean> p)
                    {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        deleteCol.setCellFactory(
                new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>()
                {
                    @Override
                    public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> p)
                    {
                        return new ButtonCell();
                    }
                });
        return deleteCol;
    }

    void addCommand(Command command)
    {
        data.add(command);
        // TODO save
    }

    void updateCommand(Command command)
    {
        // TODO update
    }

    protected class ButtonCell extends TableCell<Object, Boolean>
    {

        final Button cellButton = new Button("X");

        ButtonCell()
        {
            cellButton.setOnAction((ActionEvent t) -> {
                data.remove(getTableRow().getIndex());
                mwc.updateStatusBarMessage("Element deleted");
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty)
        {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }
}
