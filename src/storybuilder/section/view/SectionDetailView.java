package storybuilder.section.view;

import javafx.scene.control.CheckBox;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionDetailView extends AbstractDetailView
{

    private CheckBox endingField;

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
    protected void setFields() throws SBException
    {
        final Section section = (Section) element;
        section.refreshElements(cache.getStory());

        endingField = new CheckBox("Ending?");
        endingField.setSelected(section.isEnding());
        add(endingField);

        paragraphsTable = new ParagraphsTable(section);
        add(paragraphsTable);
    }

    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        section.setEnding(endingField.isSelected());
        section.setParagraphs(paragraphsTable.getParagraphsData());
    }

    @Override
    protected void disableFields()
    {
        endingField.setDisable(true);
        paragraphsTable.setDisable(true);
    }

}
