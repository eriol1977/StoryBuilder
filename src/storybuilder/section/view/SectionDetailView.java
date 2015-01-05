package storybuilder.section.view;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionDetailView extends AbstractDetailView
{
    
    private ObservableList<Paragraph> paragraphsData;
    
    private TableView paragraphsTable;
    
    public SectionDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
        if (nameField != null) {
            nameField.setText(element.getNameWithoutPrefix());
            nameField.setDisable(true);
        }
    }
    
    @Override
    protected void setFields()
    {
        final Section section = (Section) element;
        section.refreshElements();
        paragraphsData = FXCollections.observableArrayList();
        paragraphsData.addAll(section.getParagraphs());
        paragraphsTable = new TableView();
        final double tableWidth = mwc.getScreenWidth() - 320;
        paragraphsTable.setMinWidth(tableWidth);
        paragraphsTable.setItems(paragraphsData);
        paragraphsTable.setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        final Button buttonAdd = new Button("Add");
        buttonAdd.setOnAction((ActionEvent event) -> {
            showAddParagraphDialog();
        });
        paragraphsTable.setPlaceholder(buttonAdd);
        resizeParagraphsTableHeight();
        
        paragraphsTable.setRowFactory(new Callback<TableView<Paragraph>, TableRow<Paragraph>>()
        {
            @Override
            public TableRow<Paragraph> call(TableView<Paragraph> tableView)
            {
                final TableRow<Paragraph> row = new TableRow<>();
                
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

                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                
                return row;
            }
            
            private void moveUp(final Paragraph paragraph)
            {
                int index = paragraphsData.indexOf(paragraph);
                if (index > 0) {
                    final Paragraph temp = paragraphsData.get(index - 1);
                    paragraphsData.set(index - 1, paragraph);
                    paragraphsData.set(index, temp);
                    renameParagraphs();
                    AbstractTableView.refreshTable(paragraphsTable, paragraphsData);
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
                    AbstractTableView.refreshTable(paragraphsTable, paragraphsData);
                }
            }
        });
        
        final TableColumn numberColumn = new TableColumn("#");
        numberColumn.setMaxWidth(25);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paragraphsTable.getColumns().add(numberColumn);
        
        final TableColumn column = new TableColumn("Paragraphs");
        column.setMinWidth(tableWidth - 40);
        column.setMaxWidth(tableWidth - 40);
        column.setCellValueFactory(new PropertyValueFactory<>("text"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        paragraphsTable.getColumns().add(column);
        
        add(paragraphsTable);
    }
    
    private void showAddParagraphDialog()
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mwc.getStage());
        VBox dialogVbox = new VBox(20);
        dialogVbox.setSpacing(10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        dialogVbox.getChildren().add(new Label("Paragraph text:"));
        TextField text = new TextField();
        dialogVbox.getChildren().add(text);
        Button button = new Button("Add");
        button.setOnAction((ActionEvent event) -> {
            paragraphsData.add(new Paragraph(getNewParagraphId(), text.getText(), false));
            resizeParagraphsTableHeight();
            dialog.close();
        });
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 700, 110);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private String getNewParagraphId()
    {
        int id = paragraphsData.size() + 1;
        final Section section = (Section) element;
        return section.getName() + "_" + id;
    }
    
    private void showUpdateParagraphDialog(final Paragraph paragraph)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mwc.getStage());
        VBox dialogVbox = new VBox(20);
        dialogVbox.setSpacing(10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        dialogVbox.getChildren().add(new Label("Paragraph text:"));
        TextField text = new TextField(paragraph.getText());
        dialogVbox.getChildren().add(text);
        Button button = new Button("Update");
        button.setOnAction((ActionEvent event) -> {
            paragraph.setText(text.getText());
            AbstractTableView.refreshTable(paragraphsTable, paragraphsData);
            dialog.close();
        });
        dialogVbox.getChildren().add(button);
        Scene dialogScene = new Scene(dialogVbox, 700, 110);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private void showRemoveParagraphDialog(final Paragraph paragraph)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mwc.getStage());
        VBox dialogVbox = new VBox(20);
        dialogVbox.setSpacing(10);
        dialogVbox.setPadding(new Insets(10, 10, 10, 10));
        String parText = paragraph.getText().length() > 120 ? paragraph.getText().substring(0, 120) + "..." : paragraph.getText();
        dialogVbox.getChildren().add(new Text("\"" + parText + "\""));
        dialogVbox.getChildren().add(new Label("Do you really want to delete this paragraph?"));
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
        dialogVbox.getChildren().add(buttonBox);
        Scene dialogScene = new Scene(dialogVbox, 700, 110);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private void renameParagraphs()
    {
        final String sectionName = ((Section) element).getName();
        for (int id = 1; id <= paragraphsData.size(); id++) {
            paragraphsData.get(id - 1).setName(sectionName + "_" + id);
        }
    }
    
    private void resizeParagraphsTableHeight()
    {
        final int size = paragraphsData.size();
        if (size == 0) {
            paragraphsTable.setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            paragraphsTable.setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }
    
    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        section.setParagraphs(paragraphsData);
    }
    
    @Override
    protected void disableFields()
    {
        paragraphsTable.setDisable(true);
    }
    
}
