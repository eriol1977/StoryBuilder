package storybuilder.section.view;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.MainWindowController;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.ParagraphSwitch;
import storybuilder.section.model.Section;

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
            //new AddLinkDialog(this).show();
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
                final MenuItem addMenuItem = new MenuItem("Add");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    //new AddLinkDialog(LinksTable.this).show();
                });
                final MenuItem updateMenuItem = new MenuItem("Update");
                updateMenuItem.setOnAction((ActionEvent event) -> {
                    final ParagraphSwitch parSwitch = row.getItem();
                    //new UpdateLinkDialog(LinksTable.this, link).show();
                });
                final MenuItem removeMenuItem = new MenuItem("Remove");
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

//        dialog.add(new Text("\"" + link.getReadableContent() + "\""));
//        dialog.add(new Label("Do you really want to delete this link?"));
//        final HBox buttonBox = new HBox(10);
//        Button buttonYes = new Button("Yes");
//        buttonYes.setOnAction((ActionEvent event) -> {
//            linksData.remove(link);
//            renameLinks();
//            resizeTableHeight();
//            dialog.close();
//        });
//        buttonBox.getChildren().add(buttonYes);
//        Button buttonNo = new Button("No");
//        buttonNo.setOnAction((ActionEvent event) -> {
//            dialog.close();
//        });
//        buttonBox.getChildren().add(buttonNo);
//        dialog.add(buttonBox);
        dialog.show();
    }

//    void addLink(final Link link)
//    {
//        try {
//            link.validate();
//            linksData.add(link);
//            resizeTableHeight();
//        } catch (final ValidationFailed ex) {
//            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
//        }
//    }
//
//    void updateLink(final Link link)
//    {
//        try {
//            link.validate();
//            AbstractTableView.refreshTable(this, linksData);
//        } catch (final ValidationFailed ex) {
//            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
//        }
//    }
//
//    String getNewLinkId()
//    {
//        int id = linksData.size() + 1;
//        return section.getName() + "_link_" + id;
//    }
//
//
//    private void renameLinks()
//    {
//        final String sectionName = section.getName();
//        for (int id = 1; id <= linksData.size(); id++) {
//            linksData.get(id - 1).setName(sectionName + "_link_" + id);
//        }
//    }
    private void resizeTableHeight()
    {
        final int size = switchesData.size();
        if (size == 0) {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }

    List<ParagraphSwitch> getSwitches()
    {
        return switchesData;
    }

}
