package rexel.common.simulate;

import com.alibaba.fastjson.JSONObject;
import rexel.common.constants.Constants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.URL;
import java.util.*;

public class SaxAddress  extends DefaultHandler implements Serializable {
    private List<JSONObject> jsonList = new ArrayList<>();
    private JSONObject resultJson = null;
    private String currentEleName = null;

    private Set<String> address = new HashSet<>(Arrays.asList(
            Constants.ProvCode,
            Constants.CityCode,
            Constants.CountyCode,
            Constants.Address,
            Constants.Name,
            Constants.Mobile,
            Constants.Phone
    ));

    public List<JSONObject> getJsonList() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLReader reader = null;
        try {
            SAXParser parser = factory.newSAXParser();
            reader = parser.getXMLReader();
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            return null;
        }


        SaxAddress sax = new SaxAddress();
        reader.setContentHandler(sax);

        String xmlString = readFile();
        if (xmlString == null) {
            return null;
        }

        StringReader stringReader = new StringReader(xmlString);
        InputSource input = new InputSource(stringReader);
        try {
            reader.parse(input);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }

        return sax.jsonList;
    }

    private String readFile() {
        URL url = DataRandom.class.getClassLoader().getResource("");
        if (url == null) {
            return null;
        }
//        try {
//            throw new Exception(url.getPath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        String path = url.getPath();
        String encoding = "UTF-8";
        File file = new File(path + "/" + Constants.FileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        FileInputStream  fis;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        try {
            fis.read(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentEleName = qName;
        if (Constants.AddressInfo.equals(qName)) {
            resultJson = new JSONObject();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (Constants.AddressInfo.equals(qName)) {
            jsonList.add(resultJson);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = trim(String.valueOf(ch, start, length));
        if ("".equals(value)) {
            return;
        }

        if (address.contains(currentEleName)) {
            putJson(resultJson, currentEleName, value);
        }
    }

    private String trim(String str) {
        return str.trim().replace("\\n", "");
    }

    private void putJson(JSONObject jsonObject, String key, String value) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        if (value != null && !"".equals(value.trim())) {
            jsonObject.put(key, value);
        }
    }
}
