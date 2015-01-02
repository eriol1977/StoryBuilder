package storybuilder.main;

import storybuilder.main.IStoryElement;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class StoryElement implements IStoryElement {

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
    
    public String getName()
    {
        return name.get();
    }
    
    public void setName(final String name)
    {
        this.name.set(name);
    }
}
