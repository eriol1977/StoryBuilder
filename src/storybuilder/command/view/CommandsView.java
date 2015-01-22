package storybuilder.command.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.command.model.Command;
import storybuilder.command.model.CommandComparatorByDescription;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
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
        final List<Command> commands = cache.getStory().getCommands();
        Collections.sort(commands, new CommandComparatorByDescription());
        data.addAll(commands);
    }

    @Override
    protected IStoryElement getNewElement()
    {
        return new Command("", "", "", false);
    }

    @Override
    protected AbstractDetailView showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Command((Command) element);
        final CommandDetailView commandDetailView = new CommandDetailView(isNewElement, (Command) element, this);
        layout.getChildren().add(commandDetailView);
        return commandDetailView;
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Desc.", "description", 160));
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
