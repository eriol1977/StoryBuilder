package storybuilder.section.model.minigame;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.FileManager;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameInstance extends StoryElement
{

    private MinigameKind kind;

    private String winningSectionNumber;

    private String losingSectionNumber;

    private List<String> values;

    public MinigameInstance(final String name, final MinigameKind kind,
            final String winningSectionNumber, final String losingSectionNumber,
            final List<String> values, final boolean defaultElement)
    {
        super(name, defaultElement);
        this.kind = kind;
        this.winningSectionNumber = winningSectionNumber;
        this.losingSectionNumber = losingSectionNumber;
        this.values = values;
    }

    public MinigameInstance(Node node, boolean defaultElement)
    {
        super(node, defaultElement);
        // TODO
    }

    public MinigameInstance(final MinigameInstance another)
    {
        this(another.getName(), another.getKind(), another.getWinningSectionNumber(),
                another.getLosingSectionNumber(), another.getValues(), another.isDefault());
    }

    @Override
    public String getPrefix()
    {
        return "";
    }

    @Override
    public String getContent()
    {
        // TODO
        return null;
    }

    @Override
    public void copyData(IStoryElement another)
    {
        final MinigameInstance anotherParagraph = (MinigameInstance) another;
        setName(anotherParagraph.getName());
        setKind(anotherParagraph.getKind());
        setWinningSectionNumber(anotherParagraph.getWinningSectionNumber());
        setLosingSectionNumber(anotherParagraph.getLosingSectionNumber());
        setValues(anotherParagraph.getValues());
        setDefault(anotherParagraph.isDefault());
    }

    public static List<MinigameInstance> load(final String fileName, final boolean defaultElements, final String sectionName) throws SBException
    {
        // TODO
        final List<MinigameInstance> paragraphs = new ArrayList<>();
        final Document doc = FileManager.openDocument(fileName);
        final List<Node> elements = FileManager.findElementsMatching(sectionName + "_\\d+", doc);
        elements.stream().forEach((element) -> {
            paragraphs.add(new MinigameInstance(element, defaultElements));
        });
        return paragraphs;
    }

    @Override
    public void validate() throws ValidationFailed
    {
        super.validate();
        if (kind == null) {
            throw new ValidationFailed("Minigame kind is required");
        }
        if (winningSectionNumber == null || winningSectionNumber.isEmpty()) {
            throw new ValidationFailed("Winning section number is required");
        }
        if (losingSectionNumber == null || losingSectionNumber.isEmpty()) {
            throw new ValidationFailed("Losing section number is required");
        }
        if (values == null || values.isEmpty() || values.size() != kind.getParameters().size()) {
            throw new ValidationFailed("A value is required for each minigame parameter");
        }
    }

    public MinigameKind getKind()
    {
        return kind;
    }

    public void setKind(MinigameKind kind)
    {
        this.kind = kind;
    }

    public String getWinningSectionNumber()
    {
        return winningSectionNumber;
    }

    public void setWinningSectionNumber(String winningSectionNumber)
    {
        this.winningSectionNumber = winningSectionNumber;
    }

    public String getLosingSectionNumber()
    {
        return losingSectionNumber;
    }

    public void setLosingSectionNumber(String losingSectionNumber)
    {
        this.losingSectionNumber = losingSectionNumber;
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

}
