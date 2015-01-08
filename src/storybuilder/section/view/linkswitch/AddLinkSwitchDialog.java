package storybuilder.section.view.linkswitch;

import java.util.ArrayList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import storybuilder.main.Cache;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Link;
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class AddLinkSwitchDialog extends SBDialog
{

    protected final LinkSwitchView view;

    protected final ComboBox sectionCombo;

    protected final ObservableList<Link> links;

    protected final TableView linksTable;

    protected LinkSwitch newSwitch;

    protected final Text result;

    private final Button saveButton;

    public AddLinkSwitchDialog(final LinkSwitchView view)
    {
        this.view = view;

        setMinHeight(500);
        add(new Label("Add new link switch"));

        links = FXCollections.observableArrayList();
        linksTable = new TableView(links);
        final TableColumn column = new TableColumn("Links");
        column.setMinWidth(680);
        column.setMaxWidth(680);
        column.setCellValueFactory(new PropertyValueFactory<>("readableContent"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        linksTable.getColumns().add(column);
        linksTable.setRowFactory(new Callback<TableView<Link>, TableRow<Link>>()
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
                final MenuItem addMenuItem = new MenuItem("Create new link");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    showCreateLinkDialog();
                });
                final MenuItem removeMenuItem = new MenuItem("Remove link");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    newSwitch = new LinkSwitch(view.getNewSwitchId(),
                            getSection().getNameWithoutPrefix(),
                            link.getNumber(), null, false);
                    result.setText(newSwitch.getReadableContent());
                    saveButton.setDisable(false);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(removeMenuItem);
                return contextMenu;
            }

            // TODO
            private void showCreateLinkDialog()
            {
                final SBDialog dialog = new SBDialog();
                dialog.add(new Label("CREATE"));
//                dialog.add(new Label("Text for the new paragraph:"));
//                final TextField newTextField = new TextField();
//                newTextField.setMinWidth(680);
//                dialog.add(newTextField);
                final Button button = new Button("Save");
                button.setOnAction((ActionEvent event) -> {
                    final Section section = getSection();
                    newSwitch = new LinkSwitch(view.getNewSwitchId(),
                            section.getNameWithoutPrefix(),
                            String.valueOf(section.getNextLinkNumber()),
                            new Link("", "999",
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    false),
                            false);
                    result.setText(newSwitch.getReadableContent());
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
            links.clear();
            links.addAll(getSection().getLinks());
        });

        add(sectionCombo);
        add(linksTable);

        result = new Text();
        add(result);

        saveButton = new Button("Save");
        saveButton.setDisable(true);
        saveButton.setOnAction((ActionEvent event) -> {
            view.addSwitch(newSwitch);
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
