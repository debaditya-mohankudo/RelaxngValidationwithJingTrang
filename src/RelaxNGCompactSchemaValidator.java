
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
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;


class RelaxNGCompactSchemaValidator { 
    
    private  class CustomErrorHandler extends ErrorHandlerImpl {

        private List<List<String>> allErrors = new ArrayList<List<String>>();
        public void print(String message) {
            if (message.length() != 0) {
                List<String> error = Arrays.asList(message.split(";"));
                allErrors.add(error);
            }
        }

        public List<List<String>> getAllErrors() {
            return allErrors;
        }
    }

    public static void main(String[] args) throws IOException, SAXNotRecognizedException, SAXNotSupportedException {
        RelaxNGCompactSchemaValidator obj = new RelaxNGCompactSchemaValidator();
        List<List<String>> allErros = obj.validateRnc();
        System.out.println(allErros);
        
    }

    public List<List<String>> validateRnc() throws IOException, SAXNotRecognizedException, SAXNotSupportedException {
        File schemaLocation = new File("./svg_1-2_ps.rnc");
        File svgLocation = new File("./bimi-sq-old.svg");
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        CustomErrorHandler eh = new CustomErrorHandler();
          
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

        return eh.getAllErrors();
    }

}