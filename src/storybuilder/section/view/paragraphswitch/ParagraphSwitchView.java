package storybuilder.section.view.paragraphswitch;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.MainWindowController;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.ParagraphSwitch;
import storybuilder.section.model.Section;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class ParagraphSwitchView extends TableView<ParagraphSwitch>
{

    private final Section section;

    private ObservableList<ParagraphSwitch> switchesData;

    public ParagraphSwitchView(final Section section)
    {
        this.section = section;
        switchesData = FXCollections.observableArrayList();
        switchesData.addAll(section.getParagraphSwitches());
        final double tableWidth = MainWindowController.getInstance().getScreenWidth() - 320;
        setMinWidth(tableWidth);
        setItems(switchesData);
        setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        final Button buttonAdd = new Button("Add");
        buttonAdd.setOnAction((ActionEvent event) -> {
            new AddParagraphSwitchDialog(this).show();
        });
        setPlaceholder(buttonAdd);
        resizeTableHeight();
        setRowFactory(new Callback<TableView<ParagraphSwitch>, TableRow<ParagraphSwitch>>()
        {
            @Override
            public TableRow<ParagraphSwitch> call(TableView<ParagraphSwitch> tableView)
            {
                final TableRow<ParagraphSwitch> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<ParagraphSwitch> row)
            {
                final MenuItem addMenuItem = new MenuItem("Create switch");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    new AddParagraphSwitchDialog(ParagraphSwitchView.this).show();
                });
                final MenuItem updateMenuItem = new MenuItem("Update switch");
                updateMenuItem.setOnAction((ActionEvent event) -> {
                    final ParagraphSwitch parSwitch = row.getItem();
                    new UpdateParagraphSwitchDialog(ParagraphSwitchView.this, parSwitch).show();
                });
                final MenuItem removeMenuItem = new MenuItem("Remove switch");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final ParagraphSwitch parSwitch = row.getItem();
                    showRemoveSwitchDialog(parSwitch);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
                contextMenu.getItems().add(updateMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(removeMenuItem);
                return contextMenu;
            }
        });

        final TableColumn column = new TableColumn("Switches");
        column.setMinWidth(tableWidth);
        column.setMaxWidth(tableWidth);
        column.setCellValueFactory(new PropertyValueFactory<>("readableContent"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(column);
    }

    private void showRemoveSwitchDialog(final ParagraphSwitch parSwitch)
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Text("\"" + parSwitch.getReadableContent() + "\""));
        dialog.add(new Label("Do you really want to delete this switch?"));
        final HBox buttonBox = new HBox(10);
        Button buttonYes = new Button("Yes");
        buttonYes.setOnAction((ActionEvent event) -> {
            switchesData.remove(parSwitch);
            renameSwitches();
            resizeTableHeight();
            dialog.close();
        });
        buttonBox.getChildren().add(buttonYes);
        Button buttonNo = new Button("No");
        buttonNo.setOnAction((ActionEvent event) -> {
            dialog.close();
        });
        buttonBox.getChildren().add(buttonNo);
        dialog.add(buttonBox);
        dialog.show();
    }

    void addSwitch(final ParagraphSwitch parSwitch)
    {
        try {
            parSwitch.validate();
            switchesData.add(parSwitch);
            resizeTableHeight();
        } catch (final ValidationFailed ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
        }
    }

    void updateSwitch(final ParagraphSwitch parSwitch)
    {
        try {
            parSwitch.validate();
            AbstractTableView.refreshTable(this, switchesData);
        } catch (final ValidationFailed ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
        }
    }

    String getNewSwitchId()
    {
        return section.getName() + "_switch_" + section.getNextSwitchNumber();
    }

    private void renameSwitches()
    {
        // TODO ci sono di mezzo anche i LinkSwitch, cercare di capire meglio...
        final String sectionName = section.getName();
        //final int max = section.getNextSwitchNumber() - 1;
        for (int id = 1; id <= switchesData.size(); id++) {
            switchesData.get(id - 1).setName(sectionName + "_switch_" + id);
        }
    }

    private void resizeTableHeight()
    {
        final int size = switchesData.size();
        if (size == 0) {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }

    public List<ParagraphSwitch> getSwitches()
    {
        return switchesData;
    }

}
