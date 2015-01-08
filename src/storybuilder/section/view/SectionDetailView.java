package storybuilder.section.view;

import storybuilder.section.view.paragraphswitch.ParagraphSwitchView;
import storybuilder.section.view.link.LinksTable;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Drop;
import storybuilder.section.model.Get;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionDetailView extends AbstractDetailView
{

    private final static int EXPAND_PARAGRAPHS = 1;

    private final static int EXPAND_LINKS = 2;

    private final static int EXPAND_GETS = 3;

    private final static int EXPAND_DROPS = 4;

    private final static int EXPAND_PAR_SWITCHES = 5;

    private final static String PARAGRAPHS_PANE_TITLE = "Paragraphs";

    private final static String LINKS_PANE_TITLE = "Links";

    private final static String GETS_PANE_TITLE = "Items/Events to get";

    private final static String DROPS_PANE_TITLE = "Items to drop";

    private final static String PAR_SWITCHES_PANE_TITLE = "Paragraphs to change";

    private CheckBox endingField;

    private ParagraphsTable paragraphsTable;

    private LinksTable linksTable;

    private ParagraphSwitchView paragraphSwitchView;

    private GetView getView;

    private DropView dropView;

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
        // remembers which pane was open in the previously viewed section detail
        accordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> ov, TitledPane old_val, TitledPane new_val) -> {
            if (new_val != null) {
                final SectionsView sectionsView = (SectionsView) tableView;
                final String title = accordion.getExpandedPane().getText();
                switch (title) {
                    case PARAGRAPHS_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_PARAGRAPHS);
                        break;
                    case LINKS_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_LINKS);
                        break;
                    case GETS_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_GETS);
                        break;
                    case DROPS_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_DROPS);
                        break;
                    case PAR_SWITCHES_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_PAR_SWITCHES);
                        break;
                }
            }
        });

        paragraphsTable = new ParagraphsTable(section);
        TitledPane paragraphsPane = new TitledPane(PARAGRAPHS_PANE_TITLE, paragraphsTable);
        accordion.getPanes().add(paragraphsPane);

        linksTable = new LinksTable(this, section);
        TitledPane linksPane = new TitledPane(LINKS_PANE_TITLE, linksTable);
        accordion.getPanes().add(linksPane);

        getView = new GetView(section.getGet());
        TitledPane getPane = new TitledPane(GETS_PANE_TITLE, getView);
        accordion.getPanes().add(getPane);

        dropView = new DropView(section.getDrop());
        TitledPane dropPane = new TitledPane(DROPS_PANE_TITLE, dropView);
        accordion.getPanes().add(dropPane);

        paragraphSwitchView = new ParagraphSwitchView(section);
        TitledPane parSwitchPane = new TitledPane(PAR_SWITCHES_PANE_TITLE, paragraphSwitchView);
        accordion.getPanes().add(parSwitchPane);

        // remembers which pane was open in the previously viewed section detail
        final int expandedPane = ((SectionsView) tableView).getExpandedPane();
        if (expandedPane == EXPAND_LINKS) {
            accordion.setExpandedPane(linksPane);
        } else if (expandedPane == EXPAND_GETS) {
            accordion.setExpandedPane(getPane);
        } else if (expandedPane == EXPAND_DROPS) {
            accordion.setExpandedPane(dropPane);
        } else if (expandedPane == EXPAND_PAR_SWITCHES) {
            accordion.setExpandedPane(parSwitchPane);
        } else {
            accordion.setExpandedPane(paragraphsPane);
        }

        add(accordion);
    }

    @Override
    protected void setElementValues()
    {
        final Section section = (Section) element;
        section.setEnding(endingField.isSelected());
        section.setParagraphs(paragraphsTable.getParagraphsData());
        section.setLinks(linksTable.getLinksData());
        section.setParagraphSwitches(paragraphSwitchView.getSwitches());
        updateGet(section);
        updateDrop(section);
    }

    private void updateGet(final Section section)
    {
        Get get = section.getGet();
        if (get == null && !getView.getIds().isEmpty()) {
            get = new Get(section.getName() + "_get", false, getView.getIdsArray());
            section.setGet(get);
        } else if (get != null) {
            if (getView.getIds().isEmpty()) {
                section.setGet(null);
            } else {
                get.setIds(getView.getIds());
            }
        }
    }

    private void updateDrop(final Section section)
    {
        Drop drop = section.getDrop();
        if (drop == null && !dropView.getIds().isEmpty()) {
            drop = new Drop(section.getName() + "_drop", false, dropView.getIdsArray());
            section.setDrop(drop);
        } else if (drop != null) {
            if (dropView.getIds().isEmpty()) {
                section.setDrop(null);
            } else {
                drop.setIds(dropView.getIds());
            }
        }
    }

    @Override
    protected void disableFields()
    {
        endingField.setDisable(true);
        paragraphsTable.setDisable(true);
        linksTable.setDisable(true);
        paragraphSwitchView.setDisable(true);
        getView.setDisable(true);
        dropView.setDisable(true);
    }

    public boolean isNewSection()
    {
        return this.isNewElement;
    }
}
