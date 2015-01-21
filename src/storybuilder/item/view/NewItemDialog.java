package storybuilder.item.view;

import javafx.collections.ObservableList;
import storybuilder.item.model.Item;
import storybuilder.main.Cache;
import storybuilder.main.model.StoryElement;
import storybuilder.main.view.NewElementDialog;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class NewItemDialog extends NewElementDialog
{

    public NewItemDialog(final ObservableList<String> itemList)
    {
        super(itemList, new ItemDetailView(true, new Item("", "", "", "", false), null));
        setMinWidth(800);
        setMinHeight(300);
    }

    @Override
    protected void saveElement(StoryElement element) throws SBException
    {
        Cache.getInstance().getStory().addItem((Item) element);
    }

}
