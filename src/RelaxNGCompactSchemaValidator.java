
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;


import org.xml.sax.SAXException;


class RelaxNGCompactSchemaValidator { //JingTask.Java
    private static File logFile = new File("/tmp/log.txt");
   


    public static void main(String[] args) throws IOException {
        cleanupLogFile();
        validateRnc();
        formatAllExceptions().forEach(System.out::println);
    }

    private static void cleanupLogFile() throws IOException {
        if (Files.exists(logFile.toPath())) {
            Files.delete(logFile.toPath());
        }
    }

    private static List<List<String>> formatAllExceptions() throws IOException {
        List<List<String>> allError = new ArrayList<List<String>>(); 
        
        List<String> result = Files.readAllLines(logFile.toPath());
        

        System.out.println("No of errors: " + result.size());

        Iterator<String> errorIterator = result.iterator();

        while (errorIterator.hasNext()) {
            String errorText = errorIterator.next();
            List<String> error = Arrays.asList(errorText.split(";"));

            allError.add(error);
        }

        return allError;
    }

    public static void validateRnc() throws IOException
    {
          // https://stackoverflow.com/questions/4983057/using-jing-with-google-app-engine-cant-load-schemafactory-given-relax-ng-schem
          
          File schemaLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/svg_1-2_ps.rnc");
          File svgLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/xss-mouseover.svg");
          System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");
          SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
          ErrorHandlerImpl eh = new ErrorHandlerImpl(new FileWriter(logFile));
          
        try 
        {
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(eh);
            StreamSource xmls = new StreamSource(new StringReader(Files.readString(svgLocation.toPath())));
            validator.validate(xmls);
        }
          
          catch (SAXException | IOException  e) 
        {
            // TODO Auto-generated catch block
            eh.printException(e);
        }
    }


}