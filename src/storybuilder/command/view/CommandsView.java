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
        table.setMaxWidth(202);
        layout.getChildren().add(table);

        loadData();
        table.setItems(data);
        table.getColumns().addAll(getColumn("Name", "nameWithoutPrefix", 150), getDeleteColumn());
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
            layout.getChildren().add(new CommandDetailView(new Command("", "", "", false), this));
        });

        add(layout);
    }

    protected TableColumn getColumn(final String label, final String fieldName, final double minWidth)
    {
        final TableColumn column = new TableColumn(label);
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

    void addCommand(final Command command)
    {
        data.add(command);
        cache.addCommand(command);
        mwc.updateStatusBarMessage("Element \"" + command.getNameWithoutPrefix() + "\" added");
    }

    void updateCommand(final Command command)
    {
        cache.updateCommand(command);
        mwc.updateStatusBarMessage("Element \"" + command.getNameWithoutPrefix() + "\" updated");
    }

    protected void loadData()
    {
        data.addAll(cache.getStory().getCommands());
    }

    protected class ButtonCell extends TableCell<Object, Boolean>
    {

        final Button cellButton = new Button("X");

        ButtonCell()
        {
            cellButton.setOnAction((ActionEvent t) -> {
                final Command removed = (Command) data.remove(getTableRow().getIndex());
                cache.removeCommand(removed);
                mwc.updateStatusBarMessage("Element \"" + removed.getNameWithoutPrefix() + "\" deleted");
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty)
        {
            super.updateItem(t, empty);
            if (!empty && !((Command) data.get(getTableRow().getIndex())).isDefault()) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }
}
