package storybuilder.command.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.command.model.Command;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandsView extends AbstractTableView
{

    public CommandsView()
    {
        addTitle("Commands");
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getCommands());
    }

    @Override
    protected IStoryElement getNewElement()
    {
        return new Command("", "", "", false);
    }

    @Override
    protected void showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Command((Command) element);
        layout.getChildren().add(new CommandDetailView(isNewElement, (Command) element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Name", "nameWithoutPrefix", 150));
        return columns;
    }

    @Override
    protected boolean addElementToStory(IStoryElement element)
    {
        return cache.getStory().addCommand((Command) element);
    }

    @Override
    protected boolean updateElementInStory(IStoryElement element)
    {
        return cache.getStory().updateCommand((Command) element);
    }

    @Override
    protected boolean deleteElementFromStory(IStoryElement element)
    {
        return cache.getStory().removeCommand((Command) element);
    }

}
