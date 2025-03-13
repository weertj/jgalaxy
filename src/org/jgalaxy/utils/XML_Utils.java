package org.jgalaxy.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class XML_Utils {

  static public Document newXMLDocument() {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      return doc;
    } catch(ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  static public String documentToString( Document pD ) throws TransformerException {
    StringWriter sw = new StringWriter();
    createTransformFactory().transform( new DOMSource(pD), new StreamResult(sw) );
    return sw.toString();
  }

  static public Transformer createTransformFactory() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8" );
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    return transformer;
  }


  static public String attr( Node pN, String pItem ) {
    if (pN.hasAttributes()) {
      return attr( pN.getAttributes(), pItem );
    }
    return "";
  }

  static public String attr(NamedNodeMap pNNM, String pItem ) {
    Node n = pNNM.getNamedItem(pItem);
    if (n==null) {
      return "";
    } else {
      return n.getNodeValue();
    }
  }


  static public List<Element> childElementsByName(Node pN, String pName ) {
    List<Element> nodes = new ArrayList<>(8);
    NodeList nl = pN.getChildNodes();
    for( int i=0; i<nl.getLength(); i++ ) {
      Node n = nl.item(i);
      if (n.getNodeType()==Node.ELEMENT_NODE) {
        if (pName.isBlank() || pName.equals(n.getNodeName())) {
          nodes.add( (Element)n);
        }
      }
    }
    return nodes;
  }

  static public Optional<Node> childNodeByPath(Node pN, String pPath ) {
    String[] paths = pPath.split( "/" );
    NodeList nl = pN.getChildNodes();
    for( int i=0; i<nl.getLength(); i++ ) {
      Node n = nl.item(i);
      if (paths[0].equals(n.getNodeName())) {
        if (paths.length>1) {
          return childNodeByPath( n, pPath.substring(pPath.indexOf('/')+1) );
        }
        return Optional.of(n);
      }
    }
    return Optional.empty();
  }

  static public Node rootNodeBy(String pXML ) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    ByteArrayInputStream bais = new ByteArrayInputStream( pXML.getBytes(Charset.defaultCharset()) );
    Document doc = db.parse( bais );
    bais.close();
    NodeList nlist = doc.getChildNodes();
    for( int i=0; i<nlist.getLength(); i++ ) {
      Node n = nlist.item(i );
      return n;
    }
    return null;
  }

}
