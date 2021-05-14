
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;

import org.xml.sax.SAXException;


class RelaxNGCompactSchemaValidator { 
    private static List<List<String>> allError = new ArrayList<List<String>>(); 
    
    public static class ceh extends ErrorHandlerImpl {
        public void print(String message) {
            if (message.length() != 0) {
                List<String> error = Arrays.asList(message.split(";"));
                allError.add(error);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        validateRnc();
    }

    public static void validateRnc() throws IOException {
        File schemaLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/svg_1-2_ps.rnc");
        File svgLocation = new File("/Users/debaditya.mohankudo/Documents/SPIKE-validate-BIMI-LOGO/relaxng/relaxng/xss-mouseover.svg");
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        ceh eh = new ceh();
          
        try {
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(eh);
            StreamSource xmls = new StreamSource(new StringReader(Files.readString(svgLocation.toPath())));
            validator.validate(xmls);
        }
          
        catch (SAXException | IOException  e) {
            eh.printException(e);
        }

        System.out.println(allError);
    }

}