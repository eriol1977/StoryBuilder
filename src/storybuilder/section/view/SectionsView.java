package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.main.model.IStoryElement;
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
    }

    @Override
    protected IStoryElement getNewElement() throws SBException
    {
        final int newSectionId = cache.getStory().getLastSectionId() + 1;
        return new Section(Section.PREFIX + newSectionId, false);
    }

    @Override
    protected void showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Section((Section) element);
        layout.getChildren().add(new SectionDetailView(isNewElement, element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 150));
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

    void setExpandedPane(final int expandedPane)
    {
        this.expandedPane = expandedPane;
    }

}
