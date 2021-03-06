package storybuilder.command.model;

import java.util.ArrayList;
import java.util.List;
import storybuilder.main.model.StoryElement;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Command extends StoryElement
{

    public final static String PREFIX = "c_";

    private final SimpleStringProperty keyword;

    private final SimpleStringProperty description;

    public Command(final String name, final String keyword, final String description, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.keyword = new SimpleStringProperty(keyword);
        this.description = new SimpleStringProperty(description);
    }

    public Command(final Node node, final boolean defaultElement)
    {
        super(node, defaultElement);
        int separatorIndex = textContent.indexOf(":");
        keyword = new SimpleStringProperty(textContent.substring(0, separatorIndex));
        description = new SimpleStringProperty(textContent.substring(separatorIndex + 1, textContent.length()));
    }

    public Command(final Command another)
    {
        this(another.getName(), another.getKeyword(), another.getDescription(), another.isDefault());
    }

    @Override
    public String getContent()
    {
        return getKeyword() + ":" + getDescription();
    }

    public static List<Command> load(final String fileName, final boolean defaultElements) throws SBException
    {
        final List<Command> commands = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsStartingWith(PREFIX, doc);
        elements.stream().forEach((element) -> {
            if (!defaultElements || filterDefaultElements(element)) {
                commands.add(new Command(element, defaultElements));
            }
        });
        return commands;
    }

    private static boolean filterDefaultElements(final Node element)
    {
        return ((Element)element).getAttribute("name").equals("c_yes") ||
                ((Element)element).getAttribute("name").equals("c_no") ||
                ((Element)element).getAttribute("name").equals("c_get") ||
                ((Element)element).getAttribute("name").equals("c_use") ||
                ((Element)element).getAttribute("name").equals("c_go_back") ||
                ((Element)element).getAttribute("name").equals("c_observe");
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (keyword == null || keyword.get().isEmpty()) {
            throw new ValidationFailed("Keyword must be at least one character long");
        }
        if (description == null || description.get().isEmpty()) {
            throw new ValidationFailed("Description must be at least one character long");
        }
    }

    @Override
    public void copyData(final IStoryElement another)
    {
        final Command anotherCommand = (Command) another;
        setName(anotherCommand.getName());
        setKeyword(anotherCommand.getKeyword());
        setDescription(anotherCommand.getDescription());
        setDefault(anotherCommand.isDefault());
    }

    public String getKeyword()
    {
        return keyword.get();
    }

    public String getDescription()
    {
        return description.get();
    }

    public void setKeyword(final String keyword)
    {
        this.keyword.set(keyword);
    }

    public void setDescription(final String description)
    {
        this.description.set(description);
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    @Override
    public String toString()
    {
        return getDescription();
    }

}
