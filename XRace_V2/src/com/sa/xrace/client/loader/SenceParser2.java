///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id: $
 * Last Commit:  $Author: $
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SenceParser2 {

	private SenceObj sence = new SenceObj();
	private Document doc;

	private DocumentBuilderFactory factory;
	private DocumentBuilder docBuilder;
	ArrayList<ModelObj> modelListTemp;

	public SenceParser2(InputStream is) throws ParserConfigurationException,
			SAXException, IOException {
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		docBuilder = factory.newDocumentBuilder();
		doc = docBuilder.parse(is);
		// ½âÎö³É¹¦
		System.out.println("parse successfull");
		String rootName = doc.getDocumentElement().getNodeName();
		sence.setName(rootName);
		System.out.println(rootName);
		parse();
	}

	private void parse() {
		modelListTemp = new ArrayList<ModelObj>();
		Element root = doc.getDocumentElement();
		ModelObj mObjTemp = null;
		NodeList list = root.getChildNodes();
		// mObjTemp.setFilename(root.getAttribute("name");
		for (int i = 0; i < list.getLength(); i++) {

			Node eNode = list.item(i);
			if (eNode.getNodeType() == 1) {
				mObjTemp = new ModelObj();
				modelListTemp.add(mObjTemp);
				System.out.println(((Element) eNode).getNodeName());
				System.out.println(((Element) eNode).getAttribute("Filename"));
				mObjTemp
						.setFilename(((Element) eNode).getAttribute("Filename"));
				mObjTemp.setID(((Element) eNode).getAttribute("ID"));
				mObjTemp.setType(((Element) eNode).getAttribute("Type"));
				mObjTemp.setScale(((Element) eNode).getAttribute("Scale"));
				mObjTemp.setRadius(Float.parseFloat(((Element) eNode)
						.getAttribute("Radius")));
				System.out.println(eNode.getChildNodes().getLength());

				NodeList nLocations = eNode.getChildNodes();
				for (int z = 0; z < nLocations.getLength(); z++) {
					Node nLocation = nLocations.item(z);
					if (nLocation.getNodeType() == 1) {
						LocationObj locationObj = new LocationObj();
						mObjTemp.setLocation(locationObj);
						locationObj.setSize(Integer
								.parseInt(((Element) nLocation)
										.getAttribute("size")));
						NodeList nPoints = nLocation.getChildNodes();
						int num = 0;
						Point[] points = new Point[locationObj
										.getSize()];
						locationObj.setPoint(points);
						for (int j = 0; j < nPoints.getLength(); j++) {
							Node nPoint = nPoints.item(j);

							if (nPoint.getNodeType() == 1) {
								
								
								Point point = new Point();
								points[num++] = point;
								point.setX(Float.parseFloat(((Element) nPoint)
										.getAttribute("x")));
								point.setX(Float.parseFloat(((Element) nPoint)
										.getAttribute("y")));
								point.setX(Float.parseFloat(((Element) nPoint)
										.getAttribute("z")));
								point.setX(Float.parseFloat(((Element) nPoint)
										.getAttribute("angle")));

							}

						}

					}
				}

			}

		}
		sence.setLModelList(modelListTemp);
		// return modelListTemp;
	}

	public SenceObj getScene() {
		return this.sence;
	}

	public static void main(String[] args) throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException {
		new SenceParser2(new FileInputStream("assets/scene.xml"));
	}
}
