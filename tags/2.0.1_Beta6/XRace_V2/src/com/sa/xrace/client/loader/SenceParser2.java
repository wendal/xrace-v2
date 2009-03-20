///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class SenceParser2 {

    private SenceParser2() {
    }

    public static final ArrayList<ModelObj> parse(InputStream is)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Document doc = factory.newDocumentBuilder().parse(is);
        // ½âÎö³É¹¦
        
        ArrayList<ModelObj> modelListTemp = new ArrayList<ModelObj>();
        Element root = doc.getDocumentElement();
        ModelObj mObjTemp = null;
        NodeList list = root.getChildNodes();
        // mObjTemp.setFilename(root.getAttribute("name");
        for (int i = 0; i < list.getLength(); i++) {

            Node eNode = list.item(i);
            if (eNode.getNodeType() == 1) {
                mObjTemp = new ModelObj();
                modelListTemp.add(mObjTemp);
                mObjTemp
                        .filename = ((Element) eNode).getAttribute("Filename");
                mObjTemp.ID = Integer.parseInt(((Element) eNode).getAttribute("ID"));
                mObjTemp.type = Integer.parseInt(((Element) eNode).getAttribute("Type"));
                mObjTemp.setScale(((Element) eNode).getAttribute("Scale"));

                NodeList nLocations = eNode.getChildNodes();
                for (int z = 0; z < nLocations.getLength(); z++) {
                    Node nLocation = nLocations.item(z);
                    if (nLocation.getNodeType() == 1) {
                        LocationObj locationObj = new LocationObj();
                        mObjTemp.setLocation(locationObj);
                        locationObj.size = (Integer
                                .parseInt(((Element) nLocation)
                                        .getAttribute("size")));
                        NodeList nPoints = nLocation.getChildNodes();
                        int num = 0;
                        Point[] points = new Point[locationObj.size];
                        locationObj.points = points;
                        for (int j = 0; j < nPoints.getLength(); j++) {
                            Node nPoint = nPoints.item(j);

                            if (nPoint.getNodeType() == 1) {

                                Point point = new Point();
                                points[num++] = point;
                                point.x = Float.parseFloat(((Element) nPoint)
                                        .getAttribute("x"));
                                point.y = Float.parseFloat(((Element) nPoint)
                                        .getAttribute("y"));
                                point.z = Float.parseFloat(((Element) nPoint)
                                        .getAttribute("z"));
                                point.angle = Float
                                        .parseFloat(((Element) nPoint)
                                                .getAttribute("angle"));
                            }
                        }
                    }
                }
            }
        }
        is.close();
        
        return modelListTemp;
    }
}
