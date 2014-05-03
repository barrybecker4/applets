// Copyright by Barry G. Becker, 2014. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.apps.aikido.generation;

import com.barrybecker4.common.xml.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Converts from the xml file containing all the techniques to javascript data structures.
 * @author Barry Becker
 */
public class XmlToJsConverter {

    /**
     * Set up the javascript array structures based on the xml technique hierarchy.
     * @param document
     * @return javascript string
     */
    public String generateJavaScript(Document document) {
        StringBuilder buf = new StringBuilder();

        buf.append('\n');
        buf.append("  // setup structures for grammar\n");
        buf.append("  var attacks = new Array();\n");
        buf.append("  var next = new Array();\n");
        buf.append("  var img = new Array();\n");
        buf.append("  var label = new Array();\n\n");
        buf.append("  var desc = new Array();\n\n");

        Node root = document.getDocumentElement();
        String imgPath = DomUtil.getAttribute(root, "imgpath");

        NodeList children = root.getChildNodes();
        for (int i=0; i < children.getLength(); i++) {
            Node child = children.item(i);
            NodeInfo nodeInfo = new NodeInfo(imgPath, child);
            buf.append("  attacks[").append(i).append("]='").append(nodeInfo.getId()).append("';\n");
        }

        buf.append( genJSForNode(root, imgPath));
        buf.append('\n');

        return buf.toString();
    }

    /**
     * recursively generate the javascript structures
     * @param node
     */
    private String genJSForNode(Node node,  String imgPath) {
        // first print the img and label for the node, then next pointers for all children,
        // then do the same for all its children
        StringBuilder buf = new StringBuilder();
        NodeInfo nodeInfo = new NodeInfo(imgPath, node);
        if (nodeInfo.getId() == null)  {
            System.out.println("null id for " + node.getNodeName() + ' ' + node.getNodeValue());
        }

        NodeList children = node.getChildNodes();

        String id = nodeInfo.getId();
        if (id != null) {
            buf.append("  img['").append(id).append("']='")
               .append(nodeInfo.getImage()).append("';\n");
            buf.append("  label['").append(id).append("']='")
               .append(nodeInfo.getLabel()).append("';\n");
            buf.append("  desc['").append(id).append("']='")
                    .append(nodeInfo.getDescription()).append("';\n\n");

            int len = children.getLength();
            if (len > 0) {
                buf.append("  next['").append(nodeInfo.getId()).append("']= new Array();\n");
            }

            for (int i = 0; i < len; i++) {
                Node child = children.item(i);
                buf.append("  next['").append(id).append("'][").append(i).append("]='")
                   .append(DomUtil.getAttribute(child, "id")).append("';\n");
            }
            if (len > 0) {
                buf.append('\n');
            }
        }
        for (int i=0; i < children.getLength(); i++) {
            Node child = children.item(i);
            buf.append( genJSForNode(child, imgPath));
        }

        return buf.toString();
    }
}
