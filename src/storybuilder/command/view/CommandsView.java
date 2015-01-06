package storybuilder.command.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.command.model.Command;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;
import storybuilder.validation.SBException;

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
        columns.add(getColumn("Code", "nameWithoutPrefix", 150));
        return columns;
    }

    @Override
    protected void addElementToStory(IStoryElement element) throws SBException
    {
        cache.getStory().addCommand((Command) element);
    }

    @Override
    protected void updateElementInStory(IStoryElement element) throws SBException
    {
        cache.getStory().updateCommand((Command) element);
    }

    @Override
    protected void deleteElementFromStory(IStoryElement element) throws SBException
    {
        cache.getStory().removeCommand((Command) element);
    }

}
