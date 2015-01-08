package storybuilder.section.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.Cache;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkSwitch extends StoryElement
{

    private final static String LINK = "link";

    private final static int NEW_LINK_KIND = 1;

    private final static int DELETE_LINK_KIND = 2;

    private SimpleStringProperty sectionNumber;

    private SimpleStringProperty linkNumber;

    private Link link;

    private String linkToDeleteOriginalContent;

    public LinkSwitch(final String name, final String sectionNumber, final String linkNumber, final Link link, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.sectionNumber = new SimpleStringProperty(sectionNumber);
        this.linkNumber = new SimpleStringProperty(linkNumber);
        this.link = link;
    }

    public LinkSwitch(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] switchInfo = textContent.split(":");
        this.sectionNumber = new SimpleStringProperty(switchInfo[1]);
        this.linkNumber = new SimpleStringProperty(switchInfo[2]);
        if (switchInfo.length > 3) {
            int linkInfoSize = switchInfo.length - 3;
            final String[] linkInfo = new String[linkInfoSize];
            for (int i = 0; i < linkInfoSize; i++) {
                linkInfo[i] = switchInfo[i + 3];
            }
            this.link = new Link(linkInfo);
        }
    }

    public LinkSwitch(final LinkSwitch another)
    {
        this(another.getName(), another.getSectionNumber(), another.getLinkNumber(),
                another.getLink(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return "";
    }

    @Override
    public String getContent()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(LINK).append(":");
        sb.append(getSectionNumber()).append(":");
        sb.append(getLinkNumber());
        if (getKind() == NEW_LINK_KIND) {
            sb.append(":").append(getLink().getContent());
        }
        return sb.toString();
    }

    public String getReadableContent()
    {
        final StringBuilder sb = new StringBuilder();
        if (getKind() == DELETE_LINK_KIND) {
            sb.append("Delete link \"").append(getLinkToDeleteOriginalContent()).append("\"");
            sb.append(" of section ").append(getSectionNumber());
        } else if (getKind() == NEW_LINK_KIND) {
            sb.append("Add link \"").append(getLink().getReadableContent()).append("\"");
            sb.append(" to section ").append(getSectionNumber());
        }
        return sb.toString();
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final LinkSwitch anotherSwitch = (LinkSwitch) another;
        setName(anotherSwitch.getName());
        setSectionNumber(anotherSwitch.getSectionNumber());
        setLinkNumber(anotherSwitch.getLinkNumber());
        setLink(anotherSwitch.getLink());
        setDefault(anotherSwitch.isDefault());
    }

    public static List<LinkSwitch> load(final String fileName, final boolean defaultElements, final String sectionName) throws SBException
    {
        final List<LinkSwitch> switches = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsMatching(sectionName + "_switch_\\d+", doc);
        elements.stream().forEach((element) -> {
            if (element.getTextContent().startsWith(LINK)) {
                switches.add(new LinkSwitch(element, defaultElements));
            }
        });
        return switches;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (sectionNumber == null || sectionNumber.get().isEmpty()) {
            throw new ValidationFailed("Switch must inform a section");
        }
        if (linkNumber == null || linkNumber.get().isEmpty()) {
            throw new ValidationFailed("Switch must inform a link");
        }
    }

    private int getKind()
    {
        return link == null ? DELETE_LINK_KIND : NEW_LINK_KIND;
    }

    private String getLinkToDeleteOriginalContent()
    {
        if (getKind() == NEW_LINK_KIND) {
            return "";
        }
        if (linkToDeleteOriginalContent == null) {
            final Section section = Cache.getInstance().getStory().getSection(getSectionNumber());
            final int linkIndex = Integer.valueOf(getLinkNumber()) - 1;
            if (linkIndex < section.getLinks().size()) {
                linkToDeleteOriginalContent = section.getLinks().get(linkIndex).getReadableContent();
            } else {
                linkToDeleteOriginalContent = "<<link " + (linkIndex + 1) + " generated by another switch>>";
            }
        }
        return linkToDeleteOriginalContent;
    }

    public int getNumber(final String sectionName)
    {
        final String prefix = sectionName + "_switch_";
        final int index = prefix.length();
        return Integer.valueOf(getName().substring(index, getName().length()));
    }

    public String getSectionNumber()
    {
        return sectionNumber.get();
    }

    public void setSectionNumber(final String sectionNumber)
    {
        this.sectionNumber.set(sectionNumber);
    }

    public String getLinkNumber()
    {
        return linkNumber.get();
    }

    public void setLinkNumber(final String linkNumber)
    {
        this.linkNumber.set(linkNumber);
    }

    public Link getLink()
    {
        return link;
    }

    public void setLink(Link link)
    {
        this.link = link;
    }

}
