package storybuilder.section.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Paragraph extends StoryElement
{

    private SimpleStringProperty text;

    public Paragraph(final String name, final String text, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.text = new SimpleStringProperty(text);
    }

    public Paragraph(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        this.text = new SimpleStringProperty(textContent);
    }

    public Paragraph(final Paragraph another)
    {
        this(another.getName(), another.getText(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return "";
    }

    @Override
    public String getContent()
    {
        return text.get();
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final Paragraph anotherParagraph = (Paragraph) another;
        setName(anotherParagraph.getName());
        setText(anotherParagraph.getText());
        setDefault(anotherParagraph.isDefault());
    }

    public static List<Paragraph> load(final String fileName, final boolean defaultElements, final String sectionName) throws SBException
    {
        final List<Paragraph> paragraphs = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsMatching(sectionName + "_\\d+", doc);
        elements.stream().forEach((element) -> {
            paragraphs.add(new Paragraph(element, defaultElements));
        });
        return paragraphs;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (text == null || text.get().isEmpty()) {
            throw new ValidationFailed("Paragraph text must be at least one character long");
        }
    }

    public String getText()
    {
        return text.get();
    }

    public void setText(String text)
    {
        this.text.set(text);
    }

    public String getNumber()
    {
        int index = getName().lastIndexOf("_");
        return getName().substring(index + 1, getName().length());
    }
}
