
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;


import org.xml.sax.SAXException;


class RelaxNGCompactSchemaValidator { //JingTask.Java

   


    public static void main(String[] args) throws IOException {
        List<String> result = validateRnc();
        System.out.println(formatAllExceptions(result));
    }

    private static List<List<String>> formatAllExceptions(List<String> result) {
        List<List<String>> allError = new ArrayList<List<String>>(); 
        System.out.println("No of errors: " + result.size());
        Iterator<String> errorIterator = result.iterator();
        while (errorIterator.hasNext()) {
            String errorText = errorIterator.next();
            List<String> error = Arrays.asList(errorText.split(";"));
            error.set(1, "Message: " + error.get(1));
            allError.add(error);
        }
        return allError;
    }

    public static List<String> validateRnc() throws IOException
    {
          // https://stackoverflow.com/questions/4983057/using-jing-with-google-app-engine-cant-load-schemafactory-given-relax-ng-schem
          File logFile = new File("./log.txt");
          
          File schemaLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/svg_1-2_ps.rnc");
          File svgLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/xss-mouseover.svg");
          System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");
          SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
          FileWriter sw = new FileWriter(logFile);
          ErrorHandlerImpl eh = new ErrorHandlerImpl(sw);
          
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

        List<String> result = Files.readAllLines(logFile.toPath());
        
        return result;
    }


}