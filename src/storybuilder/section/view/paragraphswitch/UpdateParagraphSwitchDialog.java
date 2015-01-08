package storybuilder.section.view.paragraphswitch;

import storybuilder.section.model.ParagraphSwitch;

/**
 *
 * @author Francesco Bertolino
 */
public class UpdateParagraphSwitchDialog extends ParagraphSwitchDialog
{

    private final ParagraphSwitch paragraphSwitch;

    public UpdateParagraphSwitchDialog(final ParagraphSwitchView view, final ParagraphSwitch paragraphSwitch)
    {
        super("Update paragraph switch", view);

        this.paragraphSwitch = paragraphSwitch;
    }

}
