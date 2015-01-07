package storybuilder.section.view;

import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Link;
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

        newSectionCombo.getSelectionModel().select(link.getSectionId());
        commands.setRightItems(link.getCommandIds());
        items.setRightItems(link.getItemIds());
        noItems.setRightItems(link.getNoItemIds());
        events.setRightItems(link.getEventIds());
        noEvents.setRightItems(link.getNoEventIds());
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
        link.setCommandIds(commands.getRightItems());
        link.setItemIds(items.getRightItems());
        link.setNoItemIds(noItems.getRightItems());
        link.setEventIds(events.getRightItems());
        link.setNoEventIds(noEvents.getRightItems());
        linksTable.updateLink(link);
        close();
    }

}
