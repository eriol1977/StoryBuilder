package storybuilder.command.model;

import storybuilder.main.StoryElement;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Francesco Bertolino
 */
public class Command extends StoryElement
{

    private final SimpleStringProperty keyword;

    private final SimpleStringProperty description;

    public Command(final String name, final String keyword, final String description)
    {
        super(name);
        this.keyword = new SimpleStringProperty(keyword);
        this.description = new SimpleStringProperty(description);
    }

    public Command(final Node node)
    {
        super(node);
        int separatorIndex = textContent.indexOf(":");
        keyword = new SimpleStringProperty(textContent.substring(0, separatorIndex));
        description = new SimpleStringProperty(textContent.substring(separatorIndex + 1, textContent.length()));
    }

    @Override
    public Element build(final Document doc)
    {
        Element command = super.build(doc);
        command.setTextContent(getKeyword() + ":" + getDescription());
        return command;
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

    public void setKeyword(final String keyword)
    {
        this.keyword.set(keyword);
    }

    public void setDescription(final String description)
    {
        this.description.set(description);
    }
}
