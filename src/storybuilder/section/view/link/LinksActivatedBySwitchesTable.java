package storybuilder.section.view.link;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import storybuilder.graph.model.struct.LinkSwitchGraphData;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class LinksActivatedBySwitchesTable extends TableView<LinkSwitchGraphData>
{

    private final Section section;

    private final ObservableList<LinkSwitchGraphData> data;

    public LinksActivatedBySwitchesTable(final Section section)
    {
        this.section = section;
        data = FXCollections.observableArrayList();
        data.addAll(section.getLinksActivatedBySwitches());
        setItems(data);
        setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        setPlaceholder(new Label("No new links activated by switches"));
        resizeTableHeight();
        
        final TableColumn column = new TableColumn("Links");
        column.setCellValueFactory(new PropertyValueFactory<>("text"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        getColumns().add(column);

        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void resizeTableHeight()
    {
        final int size = data.size();
        if (size == 0) {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * 2.5);
        } else {
            setMaxHeight(AbstractTableView.ROW_HEIGHT * (size + 1.1));
        }
    }
}
