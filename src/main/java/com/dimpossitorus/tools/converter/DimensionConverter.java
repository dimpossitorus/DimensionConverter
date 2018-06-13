package com.dimpossitorus.tools.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class DimensionConverter {

    private Document resourceFile;
    private String filePath;
    private double multiplier;

    public DimensionConverter(String filePath) {
        this.filePath = filePath;
    }

    public String getUpdatedFile(double _multiplier) {
        readXmlFile();
        multiplier = _multiplier;
        convert();
        return saveToFile();
    }


    private void readXmlFile() {
        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            resourceFile = dBuilder.parse(fXmlFile);
            resourceFile.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void convert() {
        NodeList nodeList = resourceFile.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String dimenValue = node.getTextContent();
                String dimenUnit="";
                if (!node.getTextContent().contains("@dimen")) {
                    double value;
                    try {
                        value = Double.valueOf(dimenValue);
                    } catch (NumberFormatException e) {
                        dimenUnit = dimenValue.substring(dimenValue.length() - 2, dimenValue.length());
                        dimenValue = dimenValue.substring(0, dimenValue.length() - 2);
                        value = Double.valueOf(dimenValue);
                    }
                    double newValue = value* multiplier;
                    node.setTextContent(((newValue % 1) == 0 ? String.valueOf((int) newValue) : String.format("%.2f",newValue)) + dimenUnit );
                }
            }
        }
    }

    private String saveToFile() {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            String pathname = "dimens-" + multiplier + ".xml";
            Result output = new StreamResult(new File(pathname));
            Source input = new DOMSource(resourceFile);
            transformer.transform(input, output);
            return pathname;
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
