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
public class ParagraphSwitch extends StoryElement
{

    private final static String PAR = "par";

    private final static int NEW_PARAGRAPH_KIND = 1;

    private final static int SWITCH_PARAGRAPH_KIND = 2;

    private final static int DELETE_PARAGRAPH_KIND = 3;

    private SimpleStringProperty sectionNumber;

    private SimpleStringProperty paragraphNumber;

    private SimpleStringProperty text;

    private int kind = -1;

    private String parToChangeOriginalText;

    public ParagraphSwitch(final String name, final String sectionNumber, final String paragraphNumber, final String text, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.sectionNumber = new SimpleStringProperty(sectionNumber);
        this.paragraphNumber = new SimpleStringProperty(paragraphNumber);
        this.text = new SimpleStringProperty(text);
    }

    public ParagraphSwitch(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        final String[] switchInfo = textContent.split(":");
        this.sectionNumber = new SimpleStringProperty(switchInfo[1]);
        this.paragraphNumber = new SimpleStringProperty(switchInfo[2]);
        if (switchInfo.length > 3) {
            this.text = new SimpleStringProperty(switchInfo[3]);
        }
    }

    public ParagraphSwitch(final ParagraphSwitch another)
    {
        this(another.getName(), another.getSectionNumber(), another.getParagraphNumber(),
                another.getText(), another.isDefault());
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
        sb.append(PAR).append(":");
        sb.append(getSectionNumber()).append(":");
        sb.append(getParagraphNumber());
        if (getKind() != DELETE_PARAGRAPH_KIND) {
            sb.append(":").append(getText());
        }
        return sb.toString();
    }

    public String getReadableContent()
    {
        final StringBuilder sb = new StringBuilder();
        if (getKind() == DELETE_PARAGRAPH_KIND) {
            sb.append("Delete paragraph \"").append(getParToChangeOriginalText()).append("\"");
            sb.append(" of section ").append(getSectionNumber());
        } else if (getKind() == NEW_PARAGRAPH_KIND) {
            sb.append("Add paragraph \"").append(getText()).append("\"");
            sb.append(" to section ").append(getSectionNumber());
        } else {
            sb.append("Switch paragraph \"").append(getParToChangeOriginalText()).append("\"");
            sb.append(" of section ").append(getSectionNumber());
            sb.append(" with paragraph \"").append(getText()).append("\"");
        }
        return sb.toString();
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final ParagraphSwitch anotherSwitch = (ParagraphSwitch) another;
        setName(anotherSwitch.getName());
        setSectionNumber(anotherSwitch.getSectionNumber());
        setParagraphNumber(anotherSwitch.getParagraphNumber());
        setText(anotherSwitch.getText());
        setDefault(anotherSwitch.isDefault());
    }

    public static List<ParagraphSwitch> load(final String fileName, final boolean defaultElements, final String sectionName) throws SBException
    {
        final List<ParagraphSwitch> switches = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsMatching(sectionName + "_switch_\\d+", doc);
        elements.stream().forEach((element) -> {
            if (element.getTextContent().startsWith(PAR)) {
                switches.add(new ParagraphSwitch(element, defaultElements));
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
        if (paragraphNumber == null || paragraphNumber.get().isEmpty()) {
            throw new ValidationFailed("Switch must inform a paragraph");
        }
    }

    private int getKind()
    {
        if (kind == -1) {
            if (text == null || getText().isEmpty()) {
                kind = DELETE_PARAGRAPH_KIND;
            } else {
                final Section section = Cache.getInstance().getStory().getSection(getSectionNumber());
                if (section.getParagraphs().size() < Integer.valueOf(getParagraphNumber())) {
                    kind = NEW_PARAGRAPH_KIND;
                } else {
                    kind = SWITCH_PARAGRAPH_KIND;
                }
            }
        }
        return kind;
    }

    private String getParToChangeOriginalText()
    {
        if (getKind() == NEW_PARAGRAPH_KIND) {
            return "";
        }
        if (parToChangeOriginalText == null) {
            final Section section = Cache.getInstance().getStory().getSection(getSectionNumber());
            parToChangeOriginalText = section.getParagraphs().get(Integer.valueOf(getParagraphNumber()) - 1).getText();

        }
        return parToChangeOriginalText;
    }

    public int getNumber(final String sectionName)
    {
        final String prefix = sectionName + "_switch_";
        final int index = prefix.length();
        return Integer.valueOf(getName().substring(index, getName().length()));
    }

    public String getText()
    {
        return text.get();
    }

    public void setText(String text)
    {
        this.text.set(text);
    }

    public String getSectionNumber()
    {
        return sectionNumber.get();
    }

    public void setSectionNumber(final String sectionNumber)
    {
        this.sectionNumber.set(sectionNumber);
    }

    public String getParagraphNumber()
    {
        return paragraphNumber.get();
    }

    public void setParagraphNumber(final String paragraphNumber)
    {
        this.paragraphNumber.set(paragraphNumber);
    }
}
