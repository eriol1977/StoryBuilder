package storybuilder.command.view;

import javafx.collections.ObservableList;
import storybuilder.command.model.Command;
import storybuilder.main.Cache;
import storybuilder.main.model.StoryElement;
import storybuilder.main.view.NewElementDialog;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class NewCommandDialog extends NewElementDialog
{

    public NewCommandDialog(final ObservableList<StoryElement> itemList)
    {
        super(itemList, new CommandDetailView(true, new Command("", "", "", false), null));
        setMinHeight(270);
        setTitle("New command");
    }

    @Override
    protected void saveElement(StoryElement element) throws SBException
    {
        Cache.getInstance().getStory().addCommand((Command) element);
    }

}
