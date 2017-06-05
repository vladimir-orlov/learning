package com.vorlov.money;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

/**
 * Created by vladimir.orlov on 05.06.2017.
 */
public class EuroRates {
    public static void main(String[] args) {
        String url = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Cube");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String currency = eElement.getAttribute("currency");
                    String rate = eElement.getAttribute("rate");
                    if (currency.isEmpty()) {
                        continue;
                    }
                    System.out.println("Ccurrency :" + currency);
                    System.out.println("Rate : " + rate);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
