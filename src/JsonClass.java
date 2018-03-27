
import java.io.*;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class JsonClass {

	public void reader() {

		try {		
			
			
			// get file
			String[] fileName = {"1", "2", "3", "4", "5"};
			int rnd = new Random().nextInt(fileName.length);
			String path = "d:/bkofficejson/"+fileName[rnd]+".json";
			BufferedReader br = new BufferedReader(new FileReader(path));

			// parce to json
			String jsonParce = IOUtils.toString(br);
			JSONObject jObj = new JSONObject(jsonParce);

			// get attr xml in json
			String xml = jObj.getString("xml");
			// decoded base64 to string
			String decoded = new String(DatatypeConverter.parseBase64Binary(xml));

			// search attr cNf in xml
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(decoded));
			Document doc = db.parse(is);

			NodeList nodes = doc.getElementsByTagName("ide");

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList data = element.getElementsByTagName("cNF");
				data.item(0).setTextContent("novo");
			}

			// Create new xml string
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(doc), new StreamResult(sw));

			// encoded xml
			String bytesEncoded = Base64.encode(sw.toString().getBytes());
			jObj.put("xml", bytesEncoded);
			System.out.println(jObj.getString("xml"));

		} catch (IOException | JSONException | ParserConfigurationException | SAXException | TransformerException e1) {
			e1.printStackTrace();
		}
	}

}