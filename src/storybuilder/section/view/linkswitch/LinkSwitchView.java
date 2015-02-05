package storybuilder.section.view.linkswitch;

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
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.Section;
import storybuilder.section.view.SectionDetailView;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkSwitchView extends TableView<LinkSwitch>
{

    private final SectionDetailView view;

    private final Section section;

    private ObservableList<LinkSwitch> switchesData;

    public LinkSwitchView(final SectionDetailView view, final Section section)
    {
        this.view = view;

        this.section = section;
        switchesData = FXCollections.observableArrayList();
        switchesData.addAll(section.getLinkSwitches());
        final double tableWidth = MainWindowController.getInstance().getScreenWidth() - 320;
        setMinWidth(tableWidth);
        setItems(switchesData);
        setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        final Button buttonAdd = new Button("Create switch");
        buttonAdd.setOnAction((ActionEvent event) -> {
            new AddLinkSwitchDialog(this).show();
        });
        setPlaceholder(buttonAdd);
        resizeTableHeight();
        setRowFactory(new Callback<TableView<LinkSwitch>, TableRow<LinkSwitch>>()
        {
            @Override
            public TableRow<LinkSwitch> call(TableView<LinkSwitch> tableView)
            {
                final TableRow<LinkSwitch> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<LinkSwitch> row)
            {
                final MenuItem addMenuItem = new MenuItem("Create switch");
                addMenuItem.setOnAction((ActionEvent event) -> {
                    new AddLinkSwitchDialog(LinkSwitchView.this).show();
                });
                final MenuItem removeMenuItem = new MenuItem("Remove switch");
                removeMenuItem.setOnAction((ActionEvent event) -> {
                    final LinkSwitch linkSwitch = row.getItem();
                    showRemoveSwitchDialog(linkSwitch);
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(addMenuItem);
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

    private void showRemoveSwitchDialog(final LinkSwitch linkSwitch)
    {
        final SBDialog dialog = new SBDialog();
        dialog.add(new Text("\"" + linkSwitch.getReadableContent() + "\""));
        dialog.add(new Label("Do you really want to delete this switch?"));
        final HBox buttonBox = new HBox(10);
        Button buttonYes = new Button("Yes");
        buttonYes.setOnAction((ActionEvent event) -> {
            switchesData.remove(linkSwitch);
            renameSwitches(linkSwitch.getNumber(section.getName()));
            view.save();
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

    void addSwitch(final LinkSwitch linkSwitch)
    {
        try {
            linkSwitch.validate();
            switchesData.add(linkSwitch);
            view.save();
        } catch (final ValidationFailed ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
        }
    }

    /**
     * @return Id for the new switch, considering the ones that are still being
     * edited, as well as those which were already saved in the section
     * (considering the ParagraphSwitches too).
     */
    String getNewSwitchId()
    {
        final int nextSwitchNumberFromSaved = section.getNextSwitchNumber();
        final int nextSwitchNumberFromHere = switchesData.isEmpty() ? 1
                : switchesData.stream().mapToInt(s -> s.getNumber(section.getName())).max().getAsInt() + 1;
        return section.getName() + "_switch_"
                + (nextSwitchNumberFromHere > nextSwitchNumberFromSaved ? nextSwitchNumberFromHere : nextSwitchNumberFromSaved);
    }

    /**
     * Subtracts 1 from the number of a switch, when this is greater than the
     * number of the removed switch.
     *
     * @param removedLinkNumber
     */
    private void renameSwitches(final int removedLinkNumber)
    {
        final String sectionName = section.getName();
        int number;
        for (final LinkSwitch ls : switchesData) {
            number = ls.getNumber(sectionName);
            if (number > removedLinkNumber) {
                ls.setName(sectionName + "_switch_" + (--number));
            }
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

    public List<LinkSwitch> getSwitches()
    {
        return switchesData;
    }

    Section getSection()
    {
        return this.section;
    }
}
