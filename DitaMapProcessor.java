import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class DitaMapProcessor {

    public static void main(String[] args) {
        String inputFilePath = "Maps\\Region\\ass\\countries\\angola\\content-types\\directory.ditamap";

        try {
            // Parse the input XML file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(inputFilePath));

            // Get the root element
            Element root = document.getDocumentElement();
            NodeList topicrefs = document.getElementsByTagName("topicref");
            NodeList chapters = document.getElementsByTagName("chapter");

            // Process each chapter element to create output filenames
            for (int i = 0; i < chapters.getLength(); i++) {
                Element chapter = (Element) chapters.item(i);
                String chapterId = chapter.getAttribute("id"); // Get the id attribute of chapter

                if (chapterId != null && !chapterId.isEmpty()) {
                    String chapterFilename = chapterId + ".dita";
                    System.out.println("Chapter filename: " + chapterFilename);

                    // Process each topicref element
                    for (int j = 0; j < topicrefs.getLength(); j++) {
                        Element topicref = (Element) topicrefs.item(j);
                        String href = topicref.getAttribute("href");

                        // Extract the filename from href
                        String filename = href.substring(href.lastIndexOf('/') + 1);
                        if (!filename.startsWith("countries/")) { // Skip filenames with "countries/"
                            

                            // Read content from the referenced file
                            String contentFilePath = "" + href.replace('/', File.separatorChar);
                            StringBuilder content = new StringBuilder();
                            BufferedReader reader = new BufferedReader(new FileReader(contentFilePath));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                            }
                            reader.close();

                            // Append the content to the chapter file
                            BufferedWriter writer = new BufferedWriter(new FileWriter(chapterFilename, true));
                            writer.write(content.toString());
                            writer.close();
                        }
                    }

                    // Print the id of the chapter
                    System.out.println("ID for chapter: " + chapterId);
                }
            }

            System.out.println("Content copied from inputFilePath to chapter files.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
