package storybuilder.section.view;

import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Get;
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

    private GetView getView;

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

        final Accordion accordion = new Accordion();

        paragraphsTable = new ParagraphsTable(section);
        TitledPane paragraphsPane = new TitledPane("Paragraphs", paragraphsTable);
        accordion.getPanes().add(paragraphsPane);

        getView = new GetView(section.getGet());
        TitledPane getPane = new TitledPane("Item Gets", getView);
        accordion.getPanes().add(getPane);

        accordion.setExpandedPane(paragraphsPane);
        add(accordion);

    }

    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        section.setEnding(endingField.isSelected());
        section.setParagraphs(paragraphsTable.getParagraphsData());
        updateGet(section);
    }

    private void updateGet(final Section section)
    {
        Get get = section.getGet();
        if (get == null) {
            get = new Get(section.getName() + "_get", false, getView.getIdsArray());
            section.setGet(get);
        } else {
            get.setIds(getView.getIds());
        }
    }

    @Override
    protected void disableFields()
    {
        endingField.setDisable(true);
        paragraphsTable.setDisable(true);
        getView.setDisable(true);
    }

}
