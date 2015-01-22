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
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

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

    protected AbstractDetailView detailView;

    public AbstractTableView()
    {
        layout = new HBox(10);

        table = new TableView();
        table.setMinWidth(202);
        table.setMaxWidth(202);
        table.setMinHeight(mwc.getScreenHeight() - 150);
        table.setMaxHeight(mwc.getScreenHeight() - 150);
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
                detailView = showDetailView(false, selected);
            }
        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final Button newButton = addButton("New");
        newButton.setOnAction((ActionEvent event) -> {
            try {
                detailView = showDetailView(true, getNewElement());
            } catch (SBException ex) {
                ErrorManager.showErrorMessage(ex.getFailCause());
            }
        });

        add(layout);
    }

    protected abstract IStoryElement getNewElement() throws SBException;

    protected abstract AbstractDetailView showDetailView(boolean isNewElement, final IStoryElement element);

    public void showNewElementView(final String callingSectionId, final int callingAccordionSection) throws SBException
    {
        detailView = showDetailView(true, getNewElement());
        final Button saveButton = detailView.getSaveButton();
        detailView.remove(saveButton);
        final Button backToSection = new Button("Save");
        backToSection.setOnAction((ActionEvent event) -> {
            saveButton.fire();
            mwc.switchToSection(callingSectionId, callingAccordionSection);
        });
        detailView.add(backToSection);
    }

    protected abstract List<TableColumn> getColumns();

    protected TableColumn getColumn(final String label, final String fieldName, final double width)
    {
        final TableColumn column = new TableColumn(label);
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }

    private TableColumn getDeleteColumn()
    {
        TableColumn deleteCol = new TableColumn<>("Del");
        deleteCol.setSortable(false);
        deleteCol.setMinWidth(30);
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
        try {
            element.validate();
            addElementToStory(element);
            data.add(element);
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" added");
            refresh();
            showEmptyView();
        } catch (ValidationFailed ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }
    }

    protected abstract void addElementToStory(final IStoryElement element) throws SBException;

    public void updateElement(final IStoryElement element)
    {
        try {
            element.validate();
            updateElementInStory(element);
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" updated");
            refresh();
            showEmptyView();
        } catch (ValidationFailed ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
            element.copyData(stashed);
            detailView = showDetailView(false, element);
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
            element.copyData(stashed);
            detailView = showDetailView(false, element);
        }
    }

    protected abstract void updateElementInStory(final IStoryElement element) throws SBException;

    protected abstract void loadData();

    public void deleteElement(final IStoryElement element)
    {
        try {
            deleteElementFromStory(element);
            data.remove(element);
            mwc.updateStatusBarMessage("Element \"" + element.getNameWithoutPrefix() + "\" deleted");
            refresh();
            showEmptyView();
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }
    }

    protected abstract void deleteElementFromStory(final IStoryElement element) throws SBException;

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

    private void refresh()
    {
        data.clear();
        loadData();
    }
    
    public static <T> void refreshTable(final TableView<T> table, final ObservableList<T> data)
    {
        table.setItems(null);
        table.layout();
        table.setItems(data);
    }

    public void selectElement(final IStoryElement element)
    {
        table.scrollTo(element);
        table.getSelectionModel().select(element);
    }
}
