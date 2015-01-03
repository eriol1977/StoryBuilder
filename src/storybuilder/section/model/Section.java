package storybuilder.section.model;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Section extends StoryElement
{

    public final static String PREFIX = "s_";

    private List<Paragraph> paragraphs = new ArrayList<>();

    public Section(final String name, final boolean defaultElement)
    {
        super(name, defaultElement);
    }

    public Section(final Node node, final boolean defaultElement)
    {
        super(node, defaultElement);
    }

    public Section(final Section another)
    {
        this(another.getName(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    @Override
    public String getContent()
    {
        // TODO
        return "";
    }

    @Override
    public void copyData(IStoryElement another)
    {
        // TODO
    }

    private void loadParagraphs()
    {
        if (isDefault()) {
            setParagraphs(Paragraph.load("resources/default.xml", true, this));
        } else {
            setParagraphs(Paragraph.load(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()), false, this));
        }
    }

    public static List<Section> load(final String fileName, final boolean defaultElements)
    {
        final List<Section> sections = new ArrayList<>();
        // TODO
        return sections;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        // TODO
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
