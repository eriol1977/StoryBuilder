package storybuilder.section.view;

import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionDetailView extends AbstractDetailView
{

    private ParagraphsTable paragraphsTable;

    public SectionDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
        if (nameField != null) {
            nameField.setText(element.getNameWithoutPrefix());
            nameField.setDisable(true);
        }
    }

    @Override
    protected void setFields()
    {
        final Section section = (Section) element;
        section.refreshElements();

        paragraphsTable = new ParagraphsTable(section);
        add(paragraphsTable);
    }

    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        section.setParagraphs(paragraphsTable.getParagraphsData());
    }

    @Override
    protected void disableFields()
    {
        paragraphsTable.setDisable(true);
    }

}
