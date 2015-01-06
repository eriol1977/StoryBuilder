package storybuilder.item.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.item.model.Item;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class ItemsView extends AbstractTableView
{

    public ItemsView()
    {
        addTitle("Items");
    }

    @Override
    protected IStoryElement getNewElement()
    {
        return new Item("", "", "", "", false);
    }

    @Override
    protected void showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Item((Item) element);
        layout.getChildren().add(new ItemDetailView(isNewElement, element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 150));
        return columns;
    }

    @Override
    protected void addElementToStory(final IStoryElement element) throws SBException
    {
        cache.getStory().addItem((Item) element);
    }

    @Override
    protected void updateElementInStory(final IStoryElement element) throws SBException
    {
        cache.getStory().updateItem((Item) element);
    }

    @Override
    protected void deleteElementFromStory(final IStoryElement element) throws SBException
    {
        cache.getStory().removeItem((Item) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getItems());
    }

}
