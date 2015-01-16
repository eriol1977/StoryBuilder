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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.MainWindowController;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.Section;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class ParagraphsTable extends TableView<Paragraph>
{

    private final Section section;

    private ObservableList<Paragraph> paragraphsData;

    public ParagraphsTable(final Section section)
    {
        this.section = section;
        paragraphsData = FXCollections.observableArrayList();
        paragraphsData.addAll(section.getParagraphs());
        setItems(paragraphsData);
        setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        final Button buttonAdd = new Button("Add");
        buttonAdd.setOnAction((ActionEvent event) -> {
            showAddParagraphDialog();
        });
        setPlaceholder(buttonAdd);
        resizeParagraphsTableHeight();
        setRowFactory(new Callback<TableView<Paragraph>, TableRow<Paragraph>>()
        {
            @Override
            public TableRow<Paragraph> call(TableView<Paragraph> tableView)
            {
                final TableRow<Paragraph> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<Paragraph> row)
            {
                final MenuItem addMenuItem = new MenuItem("Add");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    showAddParagraphDialog();
                });
                final MenuItem updateMenuItem = new MenuItem("Update");
                updateMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    showUpdateParagraphDialog(paragraph);
                });
                final MenuItem moveUpMenuItem = new MenuItem("Move Up");
                moveUpMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    moveUp(paragraph);
                });
                final MenuItem moveDownMenuItem = new MenuItem("Move Down");
                moveDownMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    moveDown(paragraph);
                });
                final MenuItem removeMenuItem = new MenuItem("Remove");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    showRemoveParagraphDialog(paragraph);
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

            private void moveUp(final Paragraph paragraph)
            {
                int index = paragraphsData.indexOf(paragraph);
                if (index > 0) {
                    final Paragraph temp = paragraphsData.get(index - 1);
                    paragraphsData.set(index - 1, paragraph);
                    paragraphsData.set(index, temp);
                    renameParagraphs();
                    AbstractTableView.refreshTable(ParagraphsTable.this, paragraphsData);
                }
            }

            private void moveDown(final Paragraph paragraph)
            {
                int index = paragraphsData.indexOf(paragraph);
                if (index < paragraphsData.size() - 1) {
                    final Paragraph temp = paragraphsData.get(index + 1);
                    paragraphsData.set(index + 1, paragraph);
                    paragraphsData.set(index, temp);
                    renameParagraphs();
                    AbstractTableView.refreshTable(ParagraphsTable.this, paragraphsData);
                }
            }
        });

        final TableColumn numberColumn = new TableColumn("#");
        numberColumn.setMaxWidth(25);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(numberColumn);

        final TableColumn column = new TableColumn("Paragraphs");
        column.setCellValueFactory(new PropertyValueFactory<>("text"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(column);
        
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void showAddParagraphDialog()
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Label("Paragraph text:"));
        TextField text = new TextField();
        dialog.add(text);
        Button button = new Button("Add");
        button.setOnAction((ActionEvent event) -> {
            final Paragraph paragraph = new Paragraph(getNewParagraphId(), text.getText(), false);
            try {
                paragraph.validate();
                paragraphsData.add(paragraph);
                resizeParagraphsTableHeight();
            } catch (ValidationFailed ex) {
                MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
            }
            dialog.close();
        });
        dialog.add(button);
        dialog.show();
    }

    private void showUpdateParagraphDialog(final Paragraph paragraph)
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Label("Paragraph text:"));
        TextField text = new TextField(paragraph.getText());
        dialog.add(text);
        Button button = new Button("Update");
        button.setOnAction((ActionEvent event) -> {
            paragraph.setText(text.getText());
            try {
                paragraph.validate();
                AbstractTableView.refreshTable(this, paragraphsData);
            } catch (ValidationFailed ex) {
                MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
            }
            dialog.close();
        });
        dialog.add(button);
        dialog.show();
    }

    private void showRemoveParagraphDialog(final Paragraph paragraph)
    {
        final SBDialog dialog = new SBDialog();
        String parText = paragraph.getText().length() > 120 ? paragraph.getText().substring(0, 120) + "..." : paragraph.getText();
        dialog.add(new Text("\"" + parText + "\""));
        dialog.add(new Label("Do you really want to delete this paragraph?"));
        final HBox buttonBox = new HBox(10);
        Button buttonYes = new Button("Yes");
        buttonYes.setOnAction((ActionEvent event) -> {
            paragraphsData.remove(paragraph);
            renameParagraphs();
            resizeParagraphsTableHeight();
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

    private String getNewParagraphId()
    {
        int id = paragraphsData.size() + 1;
        return section.getName() + "_" + id;
    }

    private void renameParagraphs()
    {
        final String sectionName = section.getName();
        for (int id = 1; id <= paragraphsData.size(); id++) {
            paragraphsData.get(id - 1).setName(sectionName + "_" + id);
        }
    }

    private void resizeParagraphsTableHeight()
    {
        final int size = paragraphsData.size();
        if (size == 0) {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }

    public ObservableList<Paragraph> getParagraphsData()
    {
        return paragraphsData;
    }

}
