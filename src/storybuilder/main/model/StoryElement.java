package storybuilder.main.model;

import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class StoryElement implements IStoryElement
{

    protected final SimpleStringProperty name;

    private final SimpleBooleanProperty defaultElement;

    protected String textContent;

    public StoryElement(final String name, final boolean defaultElement)
    {
        this.name = new SimpleStringProperty(name);
        this.defaultElement = new SimpleBooleanProperty(defaultElement);
    }

    public StoryElement(final Node node, final boolean defaultElement)
    {
        Element eElement = (Element) node;
        name = new SimpleStringProperty(eElement.getAttribute("name"));
        textContent = eElement.getTextContent();
        this.defaultElement = new SimpleBooleanProperty(defaultElement);
    }

    @Override
    public Element build(final Document doc)
    {
        Element element = doc.createElement("string");
        element.setAttribute("name", getName());
        element.setTextContent(getContent());
        return element;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        if (getNameWithoutPrefix().isEmpty()) {
            throw new ValidationFailed("Name must be at least one character long");
        }
        if (name.get().contains(" ")) {
            throw new ValidationFailed("Name cannot contain empty spaces");
        }
    }

    @Override
    public String getName()
    {
        return name.get();
    }

    @Override
    public String getNameWithoutPrefix()
    {
        final String fullName = name.get();
        return fullName.substring(getPrefix().length(), fullName.length());
    }

    public void setName(final String name)
    {
        this.name.set(name);
    }

    public void setNameWithoutPrefix(final String name)
    {
        this.name.set(getPrefix() + name);
    }

    @Override
    public boolean isDefault()
    {
        return defaultElement.get();
    }

    public void setDefault(final boolean defaultElement)
    {
        this.defaultElement.set(defaultElement);
    }

    @Override
    public String toString()
    {
        return "<string name=\"" + getName() + "\">" + getContent() + "</string>";
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StoryElement other = (StoryElement) obj;
        return Objects.equals(this.name, other.name);
    }

}
