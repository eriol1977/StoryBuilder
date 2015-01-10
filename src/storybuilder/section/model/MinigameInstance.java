package storybuilder.section.model;

import java.util.ArrayList;
import storybuilder.minigame.model.MinigameKind;
import java.util.List;
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
        final String[] gameInfo = textContent.split(":");
        this.kind = Cache.getInstance().getStory().getMinigame(gameInfo[0]);
        this.winningSectionNumber = gameInfo[1];
        this.losingSectionNumber = gameInfo[2];
        this.values = new ArrayList<>(gameInfo.length - 3);
        for (int i = 3; i < gameInfo.length; i++) {
            this.values.add(gameInfo[i]);
        }
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
        final StringBuilder sb = new StringBuilder();
        sb.append(kind.getCode()).append(":");
        sb.append(winningSectionNumber).append(":");
        sb.append(losingSectionNumber).append(":");
        values.stream().forEach((value) -> {
            sb.append(value).append(":");
        });
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    public String getResume()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(kind.getTitle()).append(" (");
        sb.append("winner goes to ").append(winningSectionNumber).append(" - ");
        sb.append("loser goes to ").append(losingSectionNumber).append(")");
        return sb.toString();
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

    public static MinigameInstance load(final String fileName, final String sectionName) throws SBException
    {
        final Document doc = FileManager.openDocument(fileName);
        final Node element = FileManager.findElementNamed(sectionName + "_minigame", doc);
        if (element != null) {
            return new MinigameInstance(element, false);
        }
        return null;
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
