package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionsView extends AbstractTableView
{

    /**
     * Remembers which pane was open in the previously viewed section detail
     */
    private int expandedPane = -1;

    public SectionsView()
    {
        addTitle("Sections");

        table.setMinWidth(102);
        table.setMaxWidth(102);

        table.setRowFactory(new Callback<TableView<Section>, TableRow<Section>>()
        {
            @Override
            public TableRow<Section> call(TableView<Section> tableView)
            {
                final TableRow<Section> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<Section> row)
            {
                final MenuItem jumpMenuItem = new MenuItem("Jump to graph");
                jumpMenuItem.setOnAction((ActionEvent event) -> {
                    mwc.jumpToGraphSection(row.getItem());
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(jumpMenuItem);
                return contextMenu;
            }
        });
    }

    @Override
    protected IStoryElement getNewElement() throws SBException
    {
        final int newSectionId = cache.getStory().getNewSectionId();
        return new Section(Section.PREFIX + newSectionId, false);
    }

    @Override
    protected AbstractDetailView showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Section((Section) element);
        final SectionDetailView sectionDetailView = new SectionDetailView(isNewElement, element, this);
        layout.getChildren().add(sectionDetailView);
        return sectionDetailView;
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 60));
        return columns;
    }

    @Override
    public void addElement(IStoryElement element)
    {
        super.addElement(element);
        // new sections can appear because of new links
        data.clear();
        loadData();
    }

    @Override
    protected void addElementToStory(final IStoryElement element) throws SBException
    {
        cache.getStory().addSection((Section) element);
    }

    @Override
    public void updateElement(IStoryElement element)
    {
        super.updateElement(element);
        // new sections can appear because of new links
        data.clear();
        loadData();
    }

    @Override
    protected void updateElementInStory(final IStoryElement element) throws SBException
    {
        cache.getStory().updateSection((Section) stashed, (Section) element);
    }

    @Override
    protected void deleteElementFromStory(final IStoryElement element) throws SBException
    {
        cache.getStory().deleteSection((Section) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getSections());
    }

    int getExpandedPane()
    {
        return expandedPane;
    }

    public void setExpandedPane(final int expandedPane)
    {
        this.expandedPane = expandedPane;
    }

}
