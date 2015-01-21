package storybuilder.section.view.link;

import storybuilder.main.Cache;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Link;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class UpdateLinkDialog extends LinkDialog
{

    private final Link link;

    public UpdateLinkDialog(final LinksTable linksTable, final Link link)
    {
        super("Update link", linksTable);
        this.link = link;

        final Story story = Cache.getInstance().getStory();

        newSectionCombo.getSelectionModel().select(link.getSectionId());
        commands.setRightItems(story.getCommands(link.getCommandIds()));
        items.setRightItems(story.getItems(link.getItemIds()));
        noItems.setRightItems(story.getItems(link.getNoItemIds()));
        events.setRightItems(story.getEvents(link.getEventIds()));
        noEvents.setRightItems(story.getEvents(link.getNoEventIds()));
    }

    @Override
    protected void doSomething()
    {
        final String nextSectionId;
        try {
            nextSectionId = getNextSectionId();
        } catch (ValidationFailed ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
            return;
        } catch (SBException ex) {
            MainWindowController.getInstance().updateStatusBarMessage(ex.getFailCause());
            return;
        }

        link.setSectionId(nextSectionId);
        link.setCommandIds(commands.getSelectedElementsIds());
        link.setItemIds(items.getSelectedElementsIds());
        link.setNoItemIds(noItems.getSelectedElementsIds());
        link.setEventIds(events.getSelectedElementsIds());
        link.setNoEventIds(noEvents.getSelectedElementsIds());
        linksTable.updateLink(link);
        close();
    }

}
