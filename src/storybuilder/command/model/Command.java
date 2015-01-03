package storybuilder.command.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import storybuilder.main.StoryElement;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import storybuilder.main.FileManager;
import storybuilder.validation.ErrorManager;
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
    
    private final SimpleBooleanProperty defaultCommand;
    
    public Command(final String name, final String keyword, final String description, final boolean defaultCommand)
    {
        super(name);
        this.keyword = new SimpleStringProperty(keyword);
        this.description = new SimpleStringProperty(description);
        this.defaultCommand = new SimpleBooleanProperty(defaultCommand);
    }
    
    public Command(final Node node, final boolean defaultCommand)
    {
        super(node);
        int separatorIndex = textContent.indexOf(":");
        keyword = new SimpleStringProperty(textContent.substring(0, separatorIndex));
        description = new SimpleStringProperty(textContent.substring(separatorIndex + 1, textContent.length()));
        this.defaultCommand = new SimpleBooleanProperty(defaultCommand);
    }
    
    public Command(final Command another)
    {
        this(another.getName(), another.getKeyword(), another.getDescription(), another.isDefault());
    }
    
    @Override
    public Element build(final Document doc)
    {
        final Element command = super.build(doc);
        command.setTextContent(getContent());
        return command;
    }
    
    @Override
    public String getContent()
    {
        return getKeyword() + ":" + getDescription();
    }
    
    public static List<Command> load(final String fileName, final boolean defaultCommands)
    {
        final List<Command> commands = new ArrayList<>();
        try {
            final Document doc = FileManager.openDocument(fileName);
            final List<Node> elements = FileManager.findElementsStartingWith(PREFIX, doc);
            elements.stream().forEach((element) -> {
                commands.add(new Command(element, defaultCommands));
            });
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ErrorManager.showErrorMessage(Command.class, "Error while loading commands", ex);
        }
        return commands;
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
    
    public void copyData(final Command another)
    {
        setName(another.getName());
        setKeyword(another.getKeyword());
        setDescription(another.getDescription());
        setDefaultCommand(another.isDefault());
    }
    
    @Override
    public String toString()
    {
        return "<string name=\"" + getName() + "\">" + getKeyword() + ":" + getDescription() + "</string>";
    }
    
    public String getKeyword()
    {
        return keyword.get();
    }
    
    public String getDescription()
    {
        return description.get();
    }
    
    public boolean isDefault()
    {
        return defaultCommand.get();
    }
    
    public void setKeyword(final String keyword)
    {
        this.keyword.set(keyword);
    }
    
    public void setDescription(final String description)
    {
        this.description.set(description);
    }
    
    public void setDefaultCommand(final boolean defaultCommand)
    {
        this.defaultCommand.set(defaultCommand);
    }
    
    @Override
    public String getPrefix()
    {
        return PREFIX;
    }
    
}
