package storybuilder.join.model;

import java.util.ArrayList;
import java.util.Arrays;
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
public class Join extends StoryElement
{

    public final static String PREFIX = "j_";

    private final SimpleStringProperty description;

    private List<String> itemIds = new ArrayList<>();

    private final SimpleStringProperty sectionId;

    /**
     * Used to hold the join description while it's being manipulated, for
     * showing and updating.
     */
    private String temporarySectionText;

    public Join(final String name, final String description, final String sectionId, final boolean defaultElement, final String... itemIds)
    {
        super(name, defaultElement);
        this.description = new SimpleStringProperty(description);
        this.sectionId = new SimpleStringProperty(sectionId);
        this.itemIds.addAll(Arrays.asList(itemIds));
    }

    public Join(final Node node, final boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] itemInfo = textContent.split(":");
        description = new SimpleStringProperty(itemInfo[0]);
        final String[] loadedItemIds = itemInfo[1].split(",");
        itemIds.addAll(Arrays.asList(loadedItemIds));
        if (itemInfo.length > 2) {
            sectionId = new SimpleStringProperty(itemInfo[2]);
        } else {
            sectionId = new SimpleStringProperty("");
        }
    }

    public Join(final Join another)
    {
        this(another.getName(), another.getDescription(), another.getSectionId(), another.isDefault(), another.getItemIdsArray());
    }

    @Override
    public String getContent()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(getDescription()).append(":");
        sb.append(stringifyItemIds());
        if (!getSectionId().isEmpty()) {
            sb.append(":").append(getSectionId());
        }
        return sb.toString();
    }

    public static List<Join> load(final String fileName, final boolean defaultElements) throws SBException
    {
        final List<Join> joins = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsStartingWith(PREFIX, doc);
        elements.stream().forEach((element) -> {
            joins.add(new Join(element, defaultElements));
        });
        return joins;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (description == null || description.get().isEmpty()) {
            throw new ValidationFailed("Description must be at least one character long");
        }
        if (itemIds.size() < 2) {
            throw new ValidationFailed("A minimum of two items must be chosen.");
        }
        if (temporarySectionText == null || temporarySectionText.isEmpty()) {
            throw new ValidationFailed("Text must be at least one character long");
        }
    }

    @Override
    public void copyData(final IStoryElement another)
    {
        final Join anotherJoin = (Join) another;
        setName(anotherJoin.getName());
        setDescription(anotherJoin.getDescription());
        setItemIds(anotherJoin.getItemIds());
        setSectionId(anotherJoin.getSectionId());
        setDefault(anotherJoin.isDefault());
    }

    public String getDescription()
    {
        return description.get();
    }

    public String getSectionId()
    {
        return sectionId.get();
    }

    public List<String> getItemIds()
    {
        return itemIds;
    }

    public String[] getItemIdsArray()
    {
        final String[] array = new String[itemIds.size()];
        for (int i = 0; i < itemIds.size(); i++) {
            array[i] = itemIds.get(i);
        }
        return array;
    }

    public String stringifyItemIds()
    {
        final StringBuilder sb = new StringBuilder();
        itemIds.stream().forEach(id -> sb.append(id).append(","));
        sb.delete(sb.length() - 1, sb.length()); // deletes last ','
        return sb.toString();
    }

    public void setItemIds(final List<String> itemIds)
    {
        this.itemIds = itemIds;
    }

    public void setDescription(final String description)
    {
        this.description.set(description);
    }

    public void setSectionId(final String sectionId)
    {
        this.sectionId.set(sectionId);
    }

    public String getSectionText() throws SBException
    {
        String text = "";
        if (!getSectionId().isEmpty()) {
            final Document doc = FileManager.openDocument(FileManager.getStoryFilenameWithAbsolutePath(Cache.getInstance().getStory()));
            final Node node = FileManager.findElementNamed(Section.PREFIX + getSectionId() + "_1", doc);
            if (node != null) {
                text = node.getTextContent();
            }
        }
        return text;
    }

    public String getTemporarySectionText()
    {
        return temporarySectionText;
    }

    public void setTemporarySectionText(final String temporarySectionText)
    {
        this.temporarySectionText = temporarySectionText;
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

}
