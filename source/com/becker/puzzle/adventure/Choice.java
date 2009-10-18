package com.becker.puzzle.adventure;

import org.w3c.dom.*;
import com.becker.common.xml.*;

/**
 * A choice that you can make in a scene.
 * Immutable.
 *
 * @author Barry Becker
 */
public class Choice {

    private String description_;
    private String destination_;

    public static final String QUIT = "Quit";
    public static final String PREVIOUS_SCENE = "last scene";

    /** This choice is automatically added if there are no other choices. */
    public static final Choice QUIT_CHOICE = new Choice(Choice.QUIT, null);

    public Choice(Node choiceNode) {
        this(DomUtil.getAttribute(choiceNode, "description"),
             DomUtil.getAttribute(choiceNode, "resultScene"));
    }

    public Choice(String desc, String dest) {
        description_ = desc;
        destination_ = dest;
    }

    /**
     * @return the test shown in the choice list.
     */
    public String getDescription() {
        return description_;
    }

    /**
     * @return the name of the scene to go to if they select this choice.
     */
    public String getDestination() {
        return destination_;
    }

    public Element createElement(Document document) {
        Element choiceElem = document.createElement("choice");
        choiceElem.setAttribute("description", getDescription());
        choiceElem.setAttribute("resultScene", getDestination());
        return choiceElem;
    }
}
