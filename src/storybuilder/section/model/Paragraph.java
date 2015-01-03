package storybuilder.section.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import storybuilder.event.model.Event;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Paragraph extends StoryElement
{

    private final Section section;

    private String text;

    public Paragraph(final Section section, final String name, final String text, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.text = text;
        this.section = section;
    }

    public Paragraph(final Section section, Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        this.text = textContent;
        this.section = section;
    }

    public Paragraph(final Section anotherSection, final Paragraph another)
    {
        this(anotherSection, another.getName(), another.getText(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return section.getName() + "_";
    }

    @Override
    public String getContent()
    {
        return text;
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final Paragraph anotherParagraph = (Paragraph) another;
        setName(anotherParagraph.getName());
        setText(anotherParagraph.getText());
        setDefault(anotherParagraph.isDefault());
    }

    public static List<Paragraph> load(final String fileName, final boolean defaultElements, final Section sectionOfParagraphs)
    {
        final List<Paragraph> paragraphs = new ArrayList<>();
        try {
            final Document doc = FileManager.openDocument(fileName);
            final List<Node> elements = FileManager.findElementsStartingWith(sectionOfParagraphs.getName() + "_", doc);
            elements.stream().forEach((element) -> {
                paragraphs.add(new Paragraph(sectionOfParagraphs, element, defaultElements));
            });
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ErrorManager.showErrorMessage(Event.class, "Error while loading paragraphs of section " + sectionOfParagraphs.getName(), ex);
        }
        return paragraphs;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (text == null || text.isEmpty()) {
            throw new ValidationFailed("Paragraph text must be at least one character long");
        }
    }

    @Override
    public String toString()
    {
        return "<string name=\"" + getName() + "\">" + getText() + "</string>";
    }

    public Section getSection()
    {
        return section;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

}
