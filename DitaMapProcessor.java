import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import java.io.*;

public class DitaMapProcessor {

    public static void main(String[] args) {
        String inputFilePath = "Maps\\Region\\ass\\countries\\angola\\content-types\\directory.ditamap";
        String outputFilePath = "output.dita";
        try {
            // Parse the input XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(inputFilePath));

            // Get the root element
            Element root = document.getDocumentElement();

            // Get all the topicref elements
            NodeList topicrefs = document.getElementsByTagName("topicref");

            // Process each topicref element
            for (int i = 0; i < topicrefs.getLength(); i++) {
                Element topicref = (Element) topicrefs.item(i);
                String href = topicref.getAttribute("href");

                // Extract the filename from href
                String filename = href.substring(href.lastIndexOf('/') + 1);
                if (filename.endsWith(".dita")) {
                    // Remove the extension and add '.dita'
                    filename = filename.substring(0, filename.length() - 5) + ".dita";
                } else {
                    filename += ".dita";
                }

                // Read content from the referenced file
                String contentFilePath = "" + href.replace('/', File.separatorChar);
                StringBuilder content = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(contentFilePath));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();

                // Append the content to the output file
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true));
                writer.write(content.toString());
                writer.close();
            }

            System.out.println("Content copied from inputFilePath to " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
