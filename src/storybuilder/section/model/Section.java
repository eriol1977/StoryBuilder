package storybuilder.section.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.graph.model.GraphDatasource;
import storybuilder.graph.model.struct.LinkSwitchGraphData;
import storybuilder.item.model.Item;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Section extends StoryElement
{

    public final static String PREFIX = "s_";

    public final static String[] DEFAULT_SECTION_NAMES = {"home", "help", "end", "quit"};

    private List<Paragraph> paragraphs = new ArrayList<>();

    private List<Link> links = new ArrayList<>();

    private List<ParagraphSwitch> paragraphSwitches = new ArrayList<>();

    private List<LinkSwitch> linkSwitches = new ArrayList<>();

    private boolean ending = false;

    private MinigameInstance minigame;

    private Get get;

    private Drop drop;

    public Section(final String name, final boolean defaultElement)
    {
        super(name, defaultElement);
    }

    public Section(final Section another)
    {
        this(another.getName(), another.isDefault());
        setParagraphs(another.getParagraphs());
        setLinks(another.getLinks());
        setParagraphSwitches(another.getParagraphSwitches());
        setLinkSwitches(another.getLinkSwitches());
        setMinigame(another.getMinigame());
        setGet(another.getGet());
        setDrop(another.getDrop());
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    @Override
    public String getNameWithoutPrefix()
    {
        if (isDefault()) {
            return getName();
        }
        return super.getNameWithoutPrefix();
    }

    @Override
    public String getContent()
    {
        return "";
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final Section anotherSection = (Section) another;
        setName(anotherSection.getName());
        setEnding(anotherSection.isEnding());
        setParagraphs(anotherSection.getParagraphs());
        setLinks(anotherSection.getLinks());
        setParagraphSwitches(anotherSection.getParagraphSwitches());
        setLinkSwitches(anotherSection.getLinkSwitches());
        setMinigame(anotherSection.getMinigame());
        setGet(anotherSection.getGet());
        setDrop(anotherSection.getDrop());
        setDefault(anotherSection.isDefault());
    }

    public static List<Section> loadDefault() throws SBException
    {
        final List<Section> defaultSections = new ArrayList<>();
        Section section;
        for (String defaultSectionName : DEFAULT_SECTION_NAMES) {
            section = new Section(defaultSectionName, true);
            section.loadParagraphs();
            section.loadLinks();
            section.loadParagraphSwitches();
            section.loadLinkSwitches();
            defaultSections.add(section);
        }
        return defaultSections;
    }

    public static List<Section> load(final Story story) throws SBException
    {
        final List<Section> sections = new ArrayList<>();
        final int lastSectionId = story.getLastSectionId();
        Section section;
        for (int id = 1; id <= lastSectionId; id++) {
            section = new Section(PREFIX + id, false);
            section.loadIsEnding(story);
            section.loadParagraphs();
            section.loadLinks();
            section.loadParagraphSwitches();
            section.loadLinkSwitches();
            section.loadGet();
            section.loadDrop();
            section.loadMinigame();
            // if some sections have been deleted, the sections counter still
            // has the last id number used, but some of the sections don't
            // exist anymore (they don't have any paragraphs)
            if (!section.getParagraphs().isEmpty()) {
                sections.add(section);
            }
        }
        return sections;
    }

    public void refreshElements(final Story story) throws SBException
    {
        loadIsEnding(story);
        loadParagraphs();
        loadLinks();
        loadParagraphSwitches();
        loadLinkSwitches();
        loadGet();
        loadDrop();
        loadMinigame();
    }

    private void loadIsEnding(final Story story) throws SBException
    {
        final Document doc = story.getXmlDoc();
        final Node endingElement = FileManager.findElementNamed("ending", doc);
        final String[] endingSectionsIds = endingElement.getTextContent().split(",");
        for (final String id : endingSectionsIds) {
            if (id.equals(getNameWithoutPrefix())) {
                setEnding(true);
                break;
            }
        }
    }

    private void loadParagraphs() throws SBException
    {
        if (isDefault()) {
            setParagraphs(Paragraph.load("resources/default.xml", true, getName()));
        } else {
            setParagraphs(Paragraph.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), false, getName()));
        }
    }

    private void loadLinks() throws SBException
    {
        if (isDefault()) {
            setLinks(Link.load("resources/default.xml", true, getName()));
        } else {
            setLinks(Link.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), false, getName()));
        }
    }

    private void loadParagraphSwitches() throws SBException
    {
        if (isDefault()) {
            setParagraphSwitches(ParagraphSwitch.load("resources/default.xml", true, getName()));
        } else {
            setParagraphSwitches(ParagraphSwitch.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), false, getName()));
        }
    }

    private void loadLinkSwitches() throws SBException
    {
        if (isDefault()) {
            setLinkSwitches(LinkSwitch.load("resources/default.xml", true, getName()));
        } else {
            setLinkSwitches(LinkSwitch.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), false, getName()));
        }
    }

    private void loadGet() throws SBException
    {
        if (!isDefault()) {
            final Get loadedGet = Get.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), getName());
            if (loadedGet != null) {
                setGet(loadedGet);
            }
        }
    }

    private void loadDrop() throws SBException
    {
        if (!isDefault()) {
            final Drop loadedDrop = Drop.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), getName());
            if (loadedDrop != null) {
                setDrop(loadedDrop);
            }
        }
    }

    private void loadMinigame() throws SBException
    {
        if (!isDefault()) {
            final MinigameInstance loadedGame = MinigameInstance.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), getName());
            if (loadedGame != null) {
                setMinigame(loadedGame);
            }
        }
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();

        if (paragraphs.isEmpty()) {
            throw new ValidationFailed("A minimum of one paragraph is required.");
        }

        if (isEnding() && (!links.isEmpty() || minigame != null)) {
            throw new ValidationFailed("An ending section cannot have links or a minigame.");
        }

        int directLinkCount = 0;
        for (final Link link : links) {
            if (link.isDirectLink()) {
                directLinkCount++;
                if (directLinkCount > 1) {
                    throw new ValidationFailed("A maximum of one direct link is allowed.");
                }
            }
        }

        if (!links.isEmpty() && minigame != null) {
            throw new ValidationFailed("A section cannot have links and a minigame at the same time.");
        }
    }

    @Override
    public int compareTo(final StoryElement another)
    {
        if (getNameWithoutPrefix().matches("\\d+") && another.getNameWithoutPrefix().matches("\\d+")) {
            return Integer.valueOf(getNameWithoutPrefix()).compareTo(Integer.valueOf(another.getNameWithoutPrefix()));
        }
        return super.compareTo(another);
    }

    public int getNextParagraphNumber()
    {
        return paragraphs.isEmpty() ? 1
                : paragraphs.stream().mapToInt(p -> Integer.valueOf(p.getNumber())).max().getAsInt() + 1;
    }

    public int getNextLinkNumber()
    {
        return links.isEmpty() ? 1
                : links.stream().mapToInt(l -> Integer.valueOf(l.getNumber())).max().getAsInt() + 1;
    }

    /**
     * @return prossimo numero utilizzabile per salvare uno switch
     */
    public int getNextSwitchNumber()
    {
        int maxFromParagraphSwitches = paragraphSwitches.isEmpty() ? 0
                : paragraphSwitches.stream()
                .mapToInt(ps -> ps.getNumber(getName())).max().getAsInt();
        int maxFromLinkSwitches = linkSwitches.isEmpty() ? 0
                : linkSwitches.stream()
                .mapToInt(ps -> ps.getNumber(getName())).max().getAsInt();
        int max = (maxFromParagraphSwitches > maxFromLinkSwitches
                ? maxFromParagraphSwitches : maxFromLinkSwitches);
        return max + 1;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public List<Paragraph> getParagraphs()
    {
        return paragraphs;
    }

    public void setParagraphs(final List<Paragraph> paragraphs)
    {
        this.paragraphs = paragraphs;
    }

    public List<Link> getLinks()
    {
        return links;
    }

    public void setLinks(final List<Link> links)
    {
        this.links = links;
    }

    public List<ParagraphSwitch> getParagraphSwitches()
    {
        return paragraphSwitches;
    }

    public void setParagraphSwitches(final List<ParagraphSwitch> paragraphSwitches)
    {
        this.paragraphSwitches = paragraphSwitches;
    }

    public List<LinkSwitch> getLinkSwitches()
    {
        return linkSwitches;
    }

    public List<LinkSwitchGraphData> getLinksActivatedBySwitches()
    {
        final List<LinkSwitchGraphData> result = new ArrayList<>();
        final Map<Section, List<LinkSwitchGraphData>> switchConnectionsFrom
                = new GraphDatasource().getSwitchConnectionsFrom(this);
        for (List<LinkSwitchGraphData> dataList : switchConnectionsFrom.values()) {
            for (LinkSwitchGraphData data : dataList) {
                result.add(data);
            }
        }
        Collections.sort(result);
        return result;
    }

    public void setLinkSwitches(List<LinkSwitch> linkSwitches)
    {
        this.linkSwitches = linkSwitches;
    }

    public boolean isEnding()
    {
        return ending;
    }

    public void setEnding(boolean ending)
    {
        this.ending = ending;
    }

    public Get getGet()
    {
        return get;
    }

    public void setGet(final Get get)
    {
        this.get = get;
    }

    public Drop getDrop()
    {
        return drop;
    }

    public void setDrop(Drop drop)
    {
        this.drop = drop;
    }

    public MinigameInstance getMinigame()
    {
        return minigame;
    }

    public void setMinigame(MinigameInstance minigame)
    {
        this.minigame = minigame;
    }

    public Item getItem()
    {
        final List<Item> items = Cache.getInstance().getStory().getItems();
        return items.stream().filter(i -> i.getSectionId().equals(this.getNameWithoutPrefix()))
                .findFirst().orElse(null);
    }

    /**
     * For SectionsView table
     *
     * @return
     */
    public String getHasItem()
    {
        Item item = getItem();
        if (item != null) {
            return "I";
        }
        return "";
    }

    /**
     * For SectionsView table
     *
     * @return
     */
    public String getIsEnding()
    {
        if (isEnding()) {
            return "E";
        }
        return "";
    }
}
