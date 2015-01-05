package storybuilder.section.model;

import java.util.ArrayList;
import java.util.List;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.story.model.Story;
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

    public static List<Section> loadDefault()
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

    public static List<Section> load(final Story story)
    {
        final List<Section> sections = new ArrayList<>();
        final int lastSectionId = story.getLastSectionId();
        Section section;
        for (int id = 1; id <= lastSectionId; id++) {
            section = new Section(PREFIX + id, false);
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

    public void refreshElements()
    {
        loadParagraphs();
    }

    private void loadParagraphs()
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

}
