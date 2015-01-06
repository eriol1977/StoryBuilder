package storybuilder.section.model;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

    private boolean ending = false;

    public Section(final String name, final boolean defaultElement)
    {
        super(name, defaultElement);
    }

    public Section(final Section another)
    {
        this(another.getName(), another.isDefault());
        setParagraphs(another.getParagraphs());
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
        setParagraphs(anotherSection.getParagraphs());
        setDefault(anotherSection.isDefault());
    }

    public static List<Section> loadDefault() throws SBException
    {
        final List<Section> defaultSections = new ArrayList<>();
        Section section;
        for (String defaultSectionName : DEFAULT_SECTION_NAMES) {
            section = new Section(defaultSectionName, true);
            section.loadParagraphs();
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

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (paragraphs.isEmpty()) {
            throw new ValidationFailed("A minimum of one paragraph is required.");
        }
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

    public boolean isEnding()
    {
        return ending;
    }

    public void setEnding(boolean ending)
    {
        this.ending = ending;
    }

}
