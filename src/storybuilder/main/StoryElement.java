package storybuilder.main;

import java.util.Objects;
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

    protected String textContent;

    public StoryElement(final String name)
    {
        this.name = new SimpleStringProperty(name);
    }

    public StoryElement(final Node node)
    {
        Element eElement = (Element) node;
        name = new SimpleStringProperty(eElement.getAttribute("name"));
        textContent = eElement.getTextContent();
    }

    @Override
    public Element build(final Document doc)
    {
        Element element = doc.createElement("string");
        element.setAttribute("name", getName());
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
