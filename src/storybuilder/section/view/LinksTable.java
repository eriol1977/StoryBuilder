package storybuilder.section.view;

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
import javafx.util.Callback;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.MainWindowController;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Link;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class LinksTable extends TableView<Link>
{

    private final Section section;

    private ObservableList<Link> linksData;

    public LinksTable(final Section section)
    {
        this.section = section;
        linksData = FXCollections.observableArrayList();
        linksData.addAll(section.getLinks());
        final double tableWidth = MainWindowController.getInstance().getScreenWidth() - 320;
        setMinWidth(tableWidth);
        setItems(linksData);
        setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        final Button buttonAdd = new Button("Add");
        buttonAdd.setOnAction((ActionEvent event) -> {
            showAddLinkDialog();
        });
        setPlaceholder(buttonAdd);
        resizeTableHeight();
        setRowFactory(new Callback<TableView<Link>, TableRow<Link>>()
        {
            @Override
            public TableRow<Link> call(TableView<Link> tableView)
            {
                final TableRow<Link> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<Link> row)
            {
                final MenuItem addMenuItem = new MenuItem("Add");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    showAddLinkDialog();
                });
                final MenuItem updateMenuItem = new MenuItem("Update");
                updateMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    showUpdateLinkDialog(link);
                });
                final MenuItem moveUpMenuItem = new MenuItem("Move Up");
                moveUpMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    moveUp(link);
                });
                final MenuItem moveDownMenuItem = new MenuItem("Move Down");
                moveDownMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    moveDown(link);
                });
                final MenuItem removeMenuItem = new MenuItem("Remove");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    showRemoveLinkDialog(link);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
                contextMenu.getItems().add(updateMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(moveUpMenuItem);
                contextMenu.getItems().add(moveDownMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(removeMenuItem);
                return contextMenu;
            }

            private void moveUp(final Link link)
            {
                int index = linksData.indexOf(link);
                if (index > 0) {
                    final Link temp = linksData.get(index - 1);
                    linksData.set(index - 1, link);
                    linksData.set(index, temp);
                    renameLinks();
                    AbstractTableView.refreshTable(LinksTable.this, linksData);
                }
            }

            private void moveDown(final Link link)
            {
                int index = linksData.indexOf(link);
                if (index < linksData.size() - 1) {
                    final Link temp = linksData.get(index + 1);
                    linksData.set(index + 1, link);
                    linksData.set(index, temp);
                    renameLinks();
                    AbstractTableView.refreshTable(LinksTable.this, linksData);
                }
            }
        });

        final TableColumn numberColumn = new TableColumn("#");
        numberColumn.setMaxWidth(25);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(numberColumn);

        final TableColumn column = new TableColumn("Links");
        column.setMinWidth(tableWidth - 40);
        column.setMaxWidth(tableWidth - 40);
        column.setCellValueFactory(new PropertyValueFactory<>("readableContent"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(column);
    }

    // TODO
    private void showAddLinkDialog()
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Label("Add link"));
//        TextField text = new TextField();
//        dialog.add(text);
//        Button button = new Button("Add");
//        button.setOnAction((ActionEvent event) -> {
//            final Paragraph paragraph = new Paragraph(getNewParagraphId(), text.getText(), false);
//            try {
//                paragraph.validate();
//                linksData.add(paragraph);
//                resizeTableHeight();
//            } catch (ValidationFailed ex) {
//                MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
//            }
//            dialog.close();
//        });
        //dialog.add(button);
        dialog.show();
    }

    // TODO
    private void showUpdateLinkDialog(final Link link)
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Label("Update link"));
//        TextField text = new TextField(paragraph.getText());
//        dialog.add(text);
//        Button button = new Button("Update");
//        button.setOnAction((ActionEvent event) -> {
//            paragraph.setText(text.getText());
//            try {
//                paragraph.validate();
//                AbstractTableView.refreshTable(this, linksData);
//            } catch (ValidationFailed ex) {
//                MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
//            }
//            dialog.close();
//        });
//        dialog.add(button);
        dialog.show();
    }

    // TODO
    private void showRemoveLinkDialog(final Link link)
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Label("Remove link"));
//        
//        String parText = paragraph.getText().length() > 120 ? paragraph.getText().substring(0, 120) + "..." : paragraph.getText();
//        dialog.add(new Text("\"" + parText + "\""));
//        dialog.add(new Label("Do you really want to delete this paragraph?"));
//        final HBox buttonBox = new HBox(10);
//        Button buttonYes = new Button("Yes");
//        buttonYes.setOnAction((ActionEvent event) -> {
//            linksData.remove(paragraph);
//            renameParagraphs();
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

    private String getNewLinkId()
    {
        int id = linksData.size() + 1;
        return section.getName() + "_link_" + id;
    }

    private void renameLinks()
    {
        final String sectionName = section.getName();
        for (int id = 1; id <= linksData.size(); id++) {
            linksData.get(id - 1).setName(sectionName + "_link_" + id);
        }
    }

    private void resizeTableHeight()
    {
        final int size = linksData.size();
        if (size == 0) {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }

    public ObservableList<Link> getLinksData()
    {
        return linksData;
    }

}
