package dprager;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Die Klasse ist für das Auslesen eines XML-Files zuständig. Mit den gegebenen
 * Methoden werden der volle Name eine Lehrers und alle seine Gegenstände,
 * unabhängig von der Abteilung, ausgelesen.
 *
 * @author Daniel Prager
 *
 */
public class XMLReaderPrager {
    /**
     * Die Methode übernimmt den Pfad zum XML-File und die gewünschte Lehrerid im
     * Parameter. Die 2 Parameter werden jeweils an eine Methode übergeben, welche
     * für das Auslesen des Namen bzw. der Gegenstände zuständig ist. Aus den
     * Rückgabewerten dieser Methoden wird ein String "Vorname Nachname:
     * Gegenstand1, Gegenstand2, ..." gebildet und an den Aufrufer zurückgegeben
     *
     * @param file      Pfad zum XML File
     * @param teacherid Lehrer, der ausgelesen werden soll
     * @return Vorname Nachname: Gegenstand1, Gegenstand2, ..
     */
    public static String parseXML(String file, String teacherid) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // ermöglicht es, ein XML-Dokument in ein
        // DOM Dokument umzuformen
        factory.setNamespaceAware(true);

        String ausgabe = "";
        String name = "";
        String gegenstand = "";

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file); // speichert das umgewandelte XML-File in ein Document-Objekt

            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath(); // ermöglicht Zugriff auf die Expressions

            name = getLehrer(doc, xpath, teacherid);
            gegenstand = getFach(doc, xpath, teacherid);

        } catch (ParserConfigurationException e) {
            System.out.println("Fehler beim Auslesen");
        } catch (SAXException e) {
            System.out.println("Fehler beim Auslesen");
        } catch (IOException e) {
            System.out.println("Fehler beim Auslesen");
        }

        ausgabe = name+ ": " +gegenstand;
        return ausgabe;
    }

    /**
     * Die Methode liest den Vornamen und den nachnamen des angegebenen Lehrers aus.
     * Vorname und Nachname werden ungetrennt zurückgegeben und müssen daher
     * getrennt ausgelesen werden.
     *
     * @param doc
     * @param path
     * @param lehrerid
     * @return
     */
    public static String getLehrer(Document doc, XPath path, String lehrerid) {
        String name = "";
        try {
                    XPathExpression expr = path.compile("//lehrer[@id='" +lehrerid+ "']/abteilung/gegenstand/text()"); // Namen werden ausgelesen

            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET); //Die Werte werden ungetrennt in einer NodeList gespeicherr

            for (int i = 0; i < nodes.getLength(); i++) { //die for-Schleife wiederholt sich so oft, wie der Lehrer Namen hat

                if (i == 0) { //das 1. Element besitzt noch kein Trennzeichen
                    name += nodes.item(i).getNodeValue();
                } else {
                    name += " " + nodes.item(i).getNodeValue();
                }
            }
        } catch (XPathExpressionException e) {
            System.out.println("Fehler beim Auslesen, Pfad ungültig");
        }

        return name;
    }

    public static String getFach(Document doc, XPath path, String lehrerid) {
        String fach = "";
        try {
            XPathExpression expr = path.compile("//lehrer[@id='" +lehrerid+ "']/abteilung/gegenstand/text()"); // Namen werden ausgelesen

            NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET); //Die Werte werden ungetrennt in einer NodeList gespeichert

            for (int i = 0; i < nodes.getLength(); i++) { //die for-Schleife wiederholt sich so oft, wie der Lehrer Namen hat

                if (i == 0) { //das 1. Element besitzt noch kein Trennzeichen
                    fach += nodes.item(i).getNodeValue();
                } else {
                    fach += ", " + nodes.item(i).getNodeValue();
                }
            }
        } catch (XPathExpressionException e) {
            System.out.println("Fehler beim Auslesen, Pfad ungültig");
        }

        return fach;
    }
}
