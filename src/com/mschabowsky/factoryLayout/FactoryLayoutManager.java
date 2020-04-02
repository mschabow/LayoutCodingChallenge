package com.mschabowsky.factoryLayout;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactoryLayoutManager {

    Document xmlDocument;
    NodeList parentNodes;
    HashMap<String, Node> NodeIdMap = new HashMap<>();

    public FactoryLayoutManager(Path filename) throws IOException {

        loadXmlLayout(filename);

    }

    private void loadXmlLayout(Path filePath) throws IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDocument = builder.parse(filePath.toFile());
            parentNodes = xmlDocument.getDocumentElement().getChildNodes();

            int nodeCount = parentNodes.getLength();
            for (int i = 0; i < nodeCount; i++) {
                if(parentNodes.item(i).getAttributes() != null)
                {
                    Node id = parentNodes.item(i).getAttributes().getNamedItem("ID");
                    if(id != null)
                        NodeIdMap.put(id.getNodeValue(), parentNodes.item(i));
                }


            }


        }
        catch (ParserConfigurationException e) {
            //e.printStackTrace();
        } catch (SAXException e) {
            //System.out.println(e.toString());
            //e.printStackTrace();
        }
    }

    public void modifyXYValues(int x, int y){
        modifyNodeValues(parentNodes, x, y);
    }

    private void modifyNodeValues(NodeList nodes, int x, int y){
        int nodeCount = nodes.getLength();
        for (int i = 0; i < nodeCount; i++) {
            Node node = nodes.item(i);
            if(node.getAttributes() != null)
                ModifyXYAttributes(node.getAttributes(), x,y);

            if(node.getChildNodes().getLength() > 0)
                modifyNodeValues(node.getChildNodes(), x, y);
        }
    }

    private void ModifyXYAttributes(NamedNodeMap attributes, int x, int y){
        Node xNode = attributes.getNamedItem("X");
        Node yNode = attributes.getNamedItem("Y");

        if( xNode != null)
            xNode.setNodeValue(getUpdatedValueString(xNode.getNodeValue(), x));
        if( yNode != null)
            yNode.setNodeValue(getUpdatedValueString(yNode.getNodeValue(), y));
    }

    private String getUpdatedValueString(String originalValue, int modifierValue){
        int x = Integer.parseInt(originalValue);
        x+= modifierValue;
        return String.valueOf(x);
    }

    public void printXmlToConsole(){
        TransformerFactory tFactory =
                TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDocument);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void saveXml(Path filePath) {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(filePath.toFile());
            Source input = new DOMSource(xmlDocument);

            transformer.transform(input, output);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    public void printItemListToConsole() {
        for (String id:NodeIdMap.keySet()) {
            System.out.println(id);

        }

    }

    public boolean ItemIdExists(String resp) {
        if(NodeIdMap.keySet().contains(resp))
            return true;
        else return false;
    }

    public void modifyXYValues(int x, int y, String itemId) {
        ModifyXYAttributes(NodeIdMap.get(itemId).getAttributes(), x, y);
    }
}
