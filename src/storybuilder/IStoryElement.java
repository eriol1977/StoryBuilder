/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storybuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Francesco
 */
public interface IStoryElement
{
    Element build(final Document doc);
}
