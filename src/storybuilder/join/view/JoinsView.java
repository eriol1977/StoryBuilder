package storybuilder.join.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.join.model.Join;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class JoinsView extends AbstractTableView
{

    public JoinsView()
    {
        addTitle("Joins");
    }

    @Override
    protected IStoryElement getNewElement()
    {
        return new Join("", "", "", false);
    }

    @Override
    protected void showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Join((Join) element);
        layout.getChildren().add(new JoinDetailView(isNewElement, element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 150));
        return columns;
    }

    @Override
    protected boolean addElementToStory(final IStoryElement element)
    {
        return cache.getStory().addJoin((Join) element);
    }

    @Override
    protected boolean updateElementInStory(final IStoryElement element)
    {
        return cache.getStory().updateJoin((Join) element);
    }

    @Override
    protected boolean deleteElementFromStory(final IStoryElement element)
    {
        return cache.getStory().removeJoin((Join) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getJoins());
    }

}
