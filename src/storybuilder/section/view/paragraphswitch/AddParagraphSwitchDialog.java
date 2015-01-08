package storybuilder.section.view.paragraphswitch;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import storybuilder.main.Cache;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.ParagraphSwitch;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class AddParagraphSwitchDialog extends SBDialog
{

    protected final ParagraphSwitchView view;

    protected final ComboBox sectionCombo;

    protected final ObservableList<Paragraph> paragraphs;

    protected final TableView paragraphsTable;

    protected ParagraphSwitch newParagraphSwitch;

    protected final Text result;

    private final Button saveButton;

    public AddParagraphSwitchDialog(final ParagraphSwitchView view)
    {
        this.view = view;

        setMinHeight(500);
        add(new Label("Add new paragraph switch"));

        paragraphs = FXCollections.observableArrayList();
        paragraphsTable = new TableView(paragraphs);
        final TableColumn column = new TableColumn("Paragraphs");
        column.setMinWidth(680);
        column.setMaxWidth(680);
        column.setCellValueFactory(new PropertyValueFactory<>("text"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        paragraphsTable.getColumns().add(column);
        paragraphsTable.setRowFactory(new Callback<TableView<Paragraph>, TableRow<Paragraph>>()
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
                final MenuItem addMenuItem = new MenuItem("Create new paragraph");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    showCreateParagraphDialog();
                });
                final MenuItem updateMenuItem = new MenuItem("Change paragraph text");
                updateMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    showChangeParagraphDialog(paragraph);
                });
                final MenuItem removeMenuItem = new MenuItem("Remove paragraph");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final Paragraph paragraph = row.getItem();
                    newParagraphSwitch = new ParagraphSwitch(view.getNewSwitchId(),
                            getSection().getNameWithoutPrefix(),
                            paragraph.getNumber(), "", false);
                    result.setText(newParagraphSwitch.getReadableContent());
                    saveButton.setDisable(false);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
                contextMenu.getItems().add(updateMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(removeMenuItem);
                return contextMenu;
            }

            private void showChangeParagraphDialog(final Paragraph paragraph)
            {
                final SBDialog dialog = new SBDialog();
                dialog.setMinHeight(200);
                dialog.add(new Label("Original text for this paragraph:"));
                dialog.add(new Text(paragraph.getText()));
                dialog.add(new Label("New text:"));
                final TextField newTextField = new TextField();
                newTextField.setMinWidth(680);
                dialog.add(newTextField);
                final Button button = new Button("Save");
                button.setOnAction((ActionEvent event) -> {
                    newParagraphSwitch = new ParagraphSwitch(view.getNewSwitchId(),
                            getSection().getNameWithoutPrefix(),
                            paragraph.getNumber(), newTextField.getText(), false);
                    result.setText(newParagraphSwitch.getReadableContent());
                    saveButton.setDisable(false);
                    dialog.close();
                });
                dialog.add(button);
                dialog.show();
            }

            private void showCreateParagraphDialog()
            {
                final SBDialog dialog = new SBDialog();
                dialog.add(new Label("Text for the new paragraph:"));
                final TextField newTextField = new TextField();
                newTextField.setMinWidth(680);
                dialog.add(newTextField);
                final Button button = new Button("Save");
                button.setOnAction((ActionEvent event) -> {
                    final Section section = getSection();
                    newParagraphSwitch = new ParagraphSwitch(view.getNewSwitchId(),
                            section.getNameWithoutPrefix(),
                            String.valueOf(section.getNextParagraphNumber()),
                            newTextField.getText(), false);
                    result.setText(newParagraphSwitch.getReadableContent());
                    saveButton.setDisable(false);
                    dialog.close();
                });
                dialog.add(button);
                dialog.show();
            }
        });

        final ObservableList<String> sections = FXCollections.observableArrayList(
                Cache.getInstance().getStory().getSectionIds(true));
        sectionCombo = new ComboBox(sections);
        sectionCombo.setPromptText("Section");
        sectionCombo.setOnAction((event) -> {
            paragraphs.clear();
            paragraphs.addAll(getSection().getParagraphs());
        });

        add(sectionCombo);
        add(paragraphsTable);

        result = new Text();
        add(result);

        saveButton = new Button("Save");
        saveButton.setDisable(true);
        saveButton.setOnAction((ActionEvent event) -> {
            view.addSwitch(newParagraphSwitch);
            close();
        });
        add(saveButton);
    }

    private Section getSection()
    {
        return Cache.getInstance().getStory().
                getSection((String) sectionCombo.getSelectionModel().getSelectedItem());
    }

}
