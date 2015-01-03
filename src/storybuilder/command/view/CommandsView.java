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
    
    final HBox layout;
    
    private final ObservableList<IStoryElement> data = FXCollections.observableArrayList();
    
    private Command stashed;
    
    public CommandsView()
    {
        addTitle("Commands");
        
        layout = new HBox(15);
        
        final TableView table = new TableView();
        table.setMaxWidth(202);
        layout.getChildren().add(table);
        
        loadData();
        table.setItems(data);
        table.getColumns().addAll(getColumn("Name", "nameWithoutPrefix", 150), getDeleteColumn());
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            final Command selectedCommand = (Command) table.getSelectionModel().getSelectedItem();
            if (selectedCommand != null) {
                showCommandDetail(selectedCommand);
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
    
    private void showCommandDetail(final Command command)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Command(command);
        layout.getChildren().add(new CommandDetailView(command, this));
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
        final boolean result = cache.getStory().addCommand(command);
        if (result) {
            data.add(command);
            mwc.updateStatusBarMessage("Element \"" + command.getNameWithoutPrefix() + "\" added");
        }
    }
    
    void updateCommand(final Command command)
    {
        final boolean result = cache.getStory().updateCommand(command);
        if (result) {
            mwc.updateStatusBarMessage("Element \"" + command.getNameWithoutPrefix() + "\" updated");
        } else {
            command.copyData(stashed);
            showCommandDetail(command);
        }
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
                final Command command = (Command) getTableRow().getItem();
                final boolean result = cache.getStory().removeCommand(command);
                if (result) {
                    data.remove(command);
                    mwc.updateStatusBarMessage("Element \"" + command.getNameWithoutPrefix() + "\" deleted");
                }
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
