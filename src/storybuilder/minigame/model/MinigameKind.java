package storybuilder.minigame.model;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import storybuilder.main.FileManager;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameKind
{

    private final String code;

    private final String title;

    private final String clazz;

    private final List<MinigameParameter> parameters;

    public MinigameKind(String code, String title, String clazz, List<MinigameParameter> parameters)
    {
        this.code = code;
        this.title = title;
        this.clazz = clazz;
        this.parameters = parameters;
    }

    public static List<MinigameKind> load() throws SBException
    {
        final Document doc = FileManager.openDocument("resources/minigames.xml");
        final NodeList minigameNodes = doc.getElementsByTagName("minigame");
        final List<MinigameKind> minigames = new ArrayList<>(minigameNodes.getLength());
        Node node;
        Element el;
        String code;
        String title;
        String clazz;
        String definition;
        String obs;
        String placeHolder;
        NodeList parameterNodes;
        List<MinigameParameter> parameters;
        Node parametersNode;
        Node parameterNode;
        Element parameterElement;
        for (int i = 0; i < minigameNodes.getLength(); i++) {
            node = minigameNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                el = (Element) node;
                code = el.getAttribute("name");
                title = el.getElementsByTagName("title").item(0).getTextContent();
                clazz = el.getElementsByTagName("class").item(0).getTextContent();
                parametersNode = el.getElementsByTagName("parameters").item(0);
                parameterNodes = ((Element) parametersNode).getElementsByTagName("parameter");
                parameters = new ArrayList<>(parameterNodes.getLength());
                for (int j = 0; j < parameterNodes.getLength(); j++) {
                    parameterNode = parameterNodes.item(j);
                    if (parameterNode.getNodeType() == Node.ELEMENT_NODE) {
                        parameterElement = (Element) parameterNode;
                        definition = parameterElement.getTextContent();
                        obs = parameterElement.getAttribute("obs");
                        placeHolder = parameterElement.getAttribute("placeholder");
                        parameters.add(new MinigameParameter(definition, obs, placeHolder));
                    }
                }
                minigames.add(new MinigameKind(code, title, clazz, parameters));
            }
        }
        return minigames;
    }

    public String getCode()
    {
        return code;
    }

    public String getTitle()
    {
        return title;
    }

    public String getClazz()
    {
        return clazz;
    }

    public List<MinigameParameter> getParameters()
    {
        return parameters;
    }

    @Override
    public String toString()
    {
        return getTitle();
    }

}
