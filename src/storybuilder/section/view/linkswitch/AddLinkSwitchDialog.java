package storybuilder.section.view.linkswitch;

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
import storybuilder.validation.SBException;

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

    private final Button confirmButton;

    private String newSectionNumber;

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

        final Button buttonAdd = new Button("Create new link");
        buttonAdd.setDisable(true);
        buttonAdd.setOnAction((ActionEvent event) -> {
            new LinkSwitchAddLinkDialog(this).show();
        });
        linksTable.setPlaceholder(buttonAdd);

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
                    new LinkSwitchAddLinkDialog(AddLinkSwitchDialog.this).show();
                });
                final MenuItem removeMenuItem = new MenuItem("Remove link");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final Link link = row.getItem();
                    newSwitch = new LinkSwitch(view.getNewSwitchId(),
                            getSection().getNameWithoutPrefix(),
                            link.getNumber(), null, false);
                    setResult(newSwitch.getReadableContent());
                    confirmButton.setDisable(false);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
                contextMenu.getItems().add(new SeparatorMenuItem());
                contextMenu.getItems().add(removeMenuItem);
                return contextMenu;
            }
        });

        final ObservableList<String> sections = FXCollections.observableArrayList(
                Cache.getInstance().getStory().getSectionIds(true));
        sectionCombo = new ComboBox(sections);
        sectionCombo.setPromptText("Section");
        sectionCombo.setOnAction((event) -> {
            links.clear();
            links.addAll(getSection().getLinks());
            buttonAdd.setDisable(false);
        });

        add(sectionCombo);
        add(linksTable);

        result = new Text();
        add(result);

        confirmButton = new Button("Confirm");
        confirmButton.setDisable(true);
        confirmButton.setOnAction((ActionEvent event) -> {
            view.addSwitch(newSwitch);
            // creates an empty target section for the link generated by this switch
            if (newSectionNumber != null) {
                try {
                    Cache.getInstance().getStory().addNewEmptySection(newSectionNumber, "link switch in section " + view.getSection().getNameWithoutPrefix());
                } catch (SBException ex) {
                    // ignore: the section can be created manually afterwards
                }
            }
            close();
        });
        add(confirmButton);
    }

    void addCreateLinkSwitch(final Link link)
    {
        final Section section = getSection();
        newSwitch = new LinkSwitch(view.getNewSwitchId(),
                section.getNameWithoutPrefix(),
                String.valueOf(section.getNextLinkNumber()),
                link,
                false);
        setResult(newSwitch.getReadableContent());
        confirmButton.setDisable(false);
    }

    void setResult(final String message)
    {
        result.setText(message);
    }

    private Section getSection()
    {
        return Cache.getInstance().getStory().
                getSection((String) sectionCombo.getSelectionModel().getSelectedItem());
    }

    void setNewSectionNumber(String newSectionNumber)
    {
        this.newSectionNumber = newSectionNumber;
    }

}
