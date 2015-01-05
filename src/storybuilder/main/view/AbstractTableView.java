package storybuilder.main.view;

import java.util.List;
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
import javafx.scene.text.Font;
import javafx.util.Callback;
import storybuilder.main.model.IStoryElement;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class AbstractTableView extends AbstractView
{

    public final static double ROW_HEIGHT = 25;

    protected final HBox layout;

    protected final TableView table;
    
    protected final ObservableList<IStoryElement> data = FXCollections.observableArrayList();

    protected IStoryElement stashed;

    public AbstractTableView()
    {
        layout = new HBox(10);

        table = new TableView();
        table.setMaxWidth(202);
        table.setFixedCellSize(ROW_HEIGHT);
        layout.getChildren().add(table);

        loadData();
        table.setItems(data);
        final List<TableColumn> columns = getColumns();
        columns.stream().forEach((column) -> {
            table.getColumns().add(column);
        });
        table.getColumns().add(getDeleteColumn());
        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            final IStoryElement selected = (IStoryElement) table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showDetailView(false, selected);
            }
        });

        final Button newButton = addButton("New");
        newButton.setOnAction((ActionEvent event) -> {
            showDetailView(true, getNewElement());
        });

        add(layout);
    }

    protected abstract IStoryElement getNewElement();

    protected abstract void showDetailView(boolean isNewElement, final IStoryElement element);

    protected abstract List<TableColumn> getColumns();

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

    protected void showEmptyView()
    {
        table.getSelectionModel().clearSelection();
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        layout.getChildren().add(new EmptyDetailView());
    }

    public void addElement(final IStoryElement element)
    {
        final boolean result = addElementToStory(element);
        if (result) {
            data.add(element);
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" added");
            showEmptyView();
        }
    }

    protected abstract boolean addElementToStory(final IStoryElement element);

    public void updateElement(final IStoryElement element)
    {
        final boolean result = updateElementInStory(element);
        if (result) {
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" updated");
            showEmptyView();
        } else {
            element.copyData(stashed);
            showDetailView(false, element);
        }
    }

    protected abstract boolean updateElementInStory(final IStoryElement element);

    protected abstract void loadData();

    public void deleteElement(final IStoryElement element)
    {
        final boolean result = deleteElementFromStory(element);
        if (result) {
            data.remove(element);
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" deleted");
            showEmptyView();
        }
    }

    protected abstract boolean deleteElementFromStory(final IStoryElement element);

    protected class ButtonCell extends TableCell<Object, Boolean>
    {

        final Button cellButton = new Button("X");

        ButtonCell()
        {
            cellButton.setFont(new Font("Arial", 10));
            cellButton.setMaxHeight(AbstractTableView.ROW_HEIGHT - 5);
            cellButton.setOnAction((ActionEvent t) -> {
                final IStoryElement element = (IStoryElement) getTableRow().getItem();
                deleteElement(element);
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty)
        {
            super.updateItem(t, empty);
            if (!empty && !((IStoryElement) data.get(getTableRow().getIndex())).isDefault()) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }

    public static <T> void refreshTable(final TableView<T> table, final ObservableList<T> data)
    {
        table.setItems(null);
        table.layout();
        table.setItems(data);
    }
}
