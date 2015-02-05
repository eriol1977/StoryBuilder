package storybuilder.section.view;

import storybuilder.section.view.paragraphswitch.ParagraphSwitchView;
import storybuilder.section.view.link.LinksTable;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import storybuilder.item.model.Item;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.model.Drop;
import storybuilder.section.model.Get;
import storybuilder.section.model.Section;
import storybuilder.section.view.linkswitch.LinkSwitchView;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionDetailView extends AbstractDetailView
{

    public final static int EXPAND_PARAGRAPHS = 1;

    public final static int EXPAND_LINKS = 2;

    public final static int EXPAND_GETS = 3;

    public final static int EXPAND_DROPS = 4;

    public final static int EXPAND_PAR_SWITCHES = 5;

    public final static int EXPAND_LINK_SWITCHES = 6;

    public final static int EXPAND_MINIGAME = 7;

    private final static String PARAGRAPHS_PANE_TITLE = "Paragraphs";

    private final static String LINKS_PANE_TITLE = "Links";

    private final static String GETS_PANE_TITLE = "Items/Events to get";

    private final static String DROPS_PANE_TITLE = "Items to drop";

    private final static String PAR_SWITCHES_PANE_TITLE = "Paragraph switches";

    private final static String LINK_SWITCHES_PANE_TITLE = "Link switches";

    private final static String MINIGAME_PANE_TITLE = "Minigame";

    private CheckBox endingField;

    private ParagraphsTable paragraphsTable;

    private LinksTable linksTable;

    private ParagraphSwitchView paragraphSwitchView;

    private LinkSwitchView linkSwitchView;

    private GetView getView;

    private DropView dropView;

    private MinigameInstanceView minigameView;

    public SectionDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
        if (nameField != null) {
            nameField.setText(element.getNameWithoutPrefix());
            nameField.setDisable(true);
        }
        saveButton.setVisible(false);
    }

    public void save()
    {
        setElementValues();
        if (isNewElement) {
            ((StoryElement) (element)).setNameWithoutPrefix(nameField.getText());
            tableView.addElement(element);
        } else {
            tableView.updateElement(element);
        }
        tableView.selectElement(element);
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
        accordion.setMinWidth(mwc.getScreenWidth() - 200);
        accordion.setMaxWidth(mwc.getScreenWidth() - 200);

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
                    case LINK_SWITCHES_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_LINK_SWITCHES);
                        break;
                    case MINIGAME_PANE_TITLE:
                        sectionsView.setExpandedPane(EXPAND_MINIGAME);
                        break;
                }
            }
        });

        paragraphsTable = new ParagraphsTable(this, section);
        TitledPane paragraphsPane = new TitledPane(PARAGRAPHS_PANE_TITLE, paragraphsTable);
        accordion.getPanes().add(paragraphsPane);

        linksTable = new LinksTable(this, section);
        TitledPane linksPane = new TitledPane(LINKS_PANE_TITLE, linksTable);
        accordion.getPanes().add(linksPane);

        getView = new GetView(this, section);
        TitledPane getPane = new TitledPane(GETS_PANE_TITLE, getView);
        accordion.getPanes().add(getPane);

        dropView = new DropView(this, section.getDrop());
        TitledPane dropPane = new TitledPane(DROPS_PANE_TITLE, dropView);
        accordion.getPanes().add(dropPane);

        paragraphSwitchView = new ParagraphSwitchView(this, section);
        TitledPane parSwitchPane = new TitledPane(PAR_SWITCHES_PANE_TITLE, paragraphSwitchView);
        accordion.getPanes().add(parSwitchPane);

        linkSwitchView = new LinkSwitchView(this, section);
        TitledPane linkSwitchPane = new TitledPane(LINK_SWITCHES_PANE_TITLE, linkSwitchView);
        accordion.getPanes().add(linkSwitchPane);

        minigameView = new MinigameInstanceView(this, section);
        TitledPane minigamePane = new TitledPane(MINIGAME_PANE_TITLE, minigameView);
        accordion.getPanes().add(minigamePane);

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
        } else if (expandedPane == EXPAND_LINK_SWITCHES) {
            accordion.setExpandedPane(linkSwitchPane);
        } else if (expandedPane == EXPAND_MINIGAME) {
            accordion.setExpandedPane(minigamePane);
        } else {
            accordion.setExpandedPane(paragraphsPane);
        }

        add(accordion);

        final Item item = section.getItem();
        if (item != null) {
            final Button jumpButton = addButton("Jump to '" + section.getItem().getItemName() + "'");
            jumpButton.setOnAction((ActionEvent event) -> {
                mwc.jumpToItem(item);
            });
            jumpButton.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, new Insets(2))));
        }
    }

    @Override
    public void setElementValues()
    {
        final Section section = (Section) element;
        section.setEnding(endingField.isSelected());
        section.setParagraphs(paragraphsTable.getParagraphsData());
        section.setLinks(linksTable.getLinksData());
        section.setParagraphSwitches(paragraphSwitchView.getSwitches());
        section.setLinkSwitches(linkSwitchView.getSwitches());
        updateGet(section);
        updateDrop(section);
        updateMinigame(section);
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

    private void updateMinigame(final Section section)
    {
        section.setMinigame(minigameView.getMinigame());
    }

    @Override
    protected void disableFields()
    {
        endingField.setDisable(true);
        paragraphsTable.setDisable(true);
        linksTable.setDisable(true);
        paragraphSwitchView.setDisable(true);
        linkSwitchView.setDisable(true);
        getView.setDisable(true);
        dropView.setDisable(true);
        minigameView.setDisable(true);
    }

    public boolean isNewSection()
    {
        return this.isNewElement;
    }
}
