package storybuilder.section.view;

import storybuilder.main.Cache;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Link;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class AddLinkDialog extends LinkDialog
{

    public AddLinkDialog(LinksTable linksTable)
    {
        super("Add link", linksTable);
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
        final Link link = new Link(linksTable.getNewLinkId(), nextSectionId,
                commands.getRightItems(), items.getRightItems(), noItems.getRightItems(),
                events.getRightItems(), noEvents.getRightItems(), false);
        linksTable.addLink(link);

        close();
    }

}
