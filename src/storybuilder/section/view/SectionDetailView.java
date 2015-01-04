package storybuilder.section.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    
    public SectionDetailView(IStoryElement element, AbstractTableView tableView)
    {
        super(element, tableView);
    }
    
    @Override
    protected void setFields()
    {
        final Section section = (Section) element;
        paragraphsData = FXCollections.observableArrayList();
        paragraphsData.addAll(section.getParagraphs());
        paragraphsTable = new TableView();
        paragraphsTable.setItems(paragraphsData);
        paragraphsTable.setFixedCellSize(AbstractTableView.ROW_HEIGHT);
        paragraphsTable.setMaxHeight(AbstractTableView.ROW_HEIGHT * (paragraphsData.size() + 1.1));
        final TableColumn column = new TableColumn("Paragraphs");
        column.setMinWidth(mwc.getScreenWidth() - 300);
        column.setCellValueFactory(new PropertyValueFactory<>("text"));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        paragraphsTable.getColumns().add(column);
        add(paragraphsTable);
    }
    
    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        // TODO
    }
    
    @Override
    protected void disableFields()
    {
        paragraphsTable.setDisable(true);
    }
    
}
