package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionsView extends AbstractTableView
{

    public SectionsView()
    {
        addTitle("Sections");
    }

    @Override
    protected IStoryElement getNewElement()
    {
        final int newSectionId = cache.getStory().getLastSectionId() + 1;
        return new Section(Section.PREFIX + newSectionId, false);
    }

    @Override
    protected void showDetailView(final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Section((Section) element);
        layout.getChildren().add(new SectionDetailView(element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Name", "name", 150));
        return columns;
    }

    @Override
    protected boolean addElementToStory(final IStoryElement element)
    {
        return cache.getStory().addSection((Section) element);
    }

    @Override
    protected boolean updateElementInStory(final IStoryElement element)
    {
        return cache.getStory().updateSection((Section) element);
    }

    @Override
    protected boolean deleteElementFromStory(final IStoryElement element)
    {
        return cache.getStory().deleteSection((Section) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getSections());
    }

}
