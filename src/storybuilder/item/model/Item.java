package storybuilder.item.model;

import java.util.ArrayList;
import java.util.List;
import storybuilder.main.model.StoryElement;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class Item extends StoryElement
{

    public final static String PREFIX = "i_";

    private final SimpleStringProperty itemName;

    private final SimpleStringProperty itemFullName;

    private final SimpleStringProperty sectionId;

    /**
     * Used to hold the item description while it's being manipulated, for
     * showing and updating.
     */
    private String temporarySectionText;

    public Item(final String name, final String itemName, final String itemFullName, final String sectionId, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.itemName = new SimpleStringProperty(itemName);
        this.itemFullName = new SimpleStringProperty(itemFullName);
        this.sectionId = new SimpleStringProperty(sectionId);
    }

    public Item(final Node node, final boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] itemInfo = textContent.split(":");
        itemName = new SimpleStringProperty(itemInfo[0]);
        itemFullName = new SimpleStringProperty(itemInfo[1]);
        if (itemInfo.length > 2) {
            sectionId = new SimpleStringProperty(itemInfo[2]);
        } else {
            sectionId = new SimpleStringProperty("");
        }
    }

    public Item(final Item another)
    {
        this(another.getName(), another.getItemName(), another.getItemFullName(), another.getSectionId(), another.isDefault());
    }

    @Override
    public String getContent()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(getItemName()).append(":");
        sb.append(getItemFullName());
        if (!getSectionId().isEmpty()) {
            sb.append(":").append(getSectionId());
        }
        return sb.toString();
    }

    public static List<Item> load(final String fileName, final boolean defaultElements) throws SBException
    {
        final List<Item> items = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsStartingWith(PREFIX, doc);
        elements.stream().forEach((element) -> {
            items.add(new Item(element, defaultElements));
        });
        return items;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (itemName == null || itemName.get().isEmpty()) {
            throw new ValidationFailed("Name must be at least one character long");
        }
        if (itemFullName == null || itemFullName.get().isEmpty()) {
            throw new ValidationFailed("Full name must be at least one character long");
        }
    }

    @Override
    public void copyData(final IStoryElement another)
    {
        final Item anotherItem = (Item) another;
        setName(anotherItem.getName());
        setItemName(anotherItem.getItemName());
        setItemFullName(anotherItem.getItemFullName());
        setSectionId(anotherItem.getSectionId());
        setDefault(anotherItem.isDefault());
    }

    public String getItemName()
    {
        return itemName.get();
    }

    public String getItemFullName()
    {
        return itemFullName.get();
    }

    public String getSectionId()
    {
        return sectionId.get();
    }

    public void setItemName(final String itemName)
    {
        this.itemName.set(itemName);
    }

    public void setItemFullName(final String itemFullName)
    {
        this.itemFullName.set(itemFullName);
    }

    public void setSectionId(final String sectionId)
    {
        this.sectionId.set(sectionId);
    }

    public String getSectionText() throws SBException
    {
        String description = "";
        if (!getSectionId().isEmpty()) {
            final Document doc = FileManager.openDocument(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()));
            final Node node = FileManager.findElementNamed(Section.PREFIX + getSectionId() + "_1", doc);
            if (node != null) {
                description = node.getTextContent();
            }
        }
        return description;
    }

    public String getTemporarySectionText()
    {
        return temporarySectionText;
    }

    public void setTemporarySectionText(String temporarySectionText)
    {
        this.temporarySectionText = temporarySectionText;
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    @Override
    public String toString()
    {
        return getItemName();
    }

    
}
