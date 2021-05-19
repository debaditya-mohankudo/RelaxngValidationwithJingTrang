
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
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

import org.xml.sax.SAXParseException;


class RelaxNGCompactSchemaValidator { 
    
    private static class CustomErrorHandler extends ErrorHandlerImpl {

        private final List<String> errors = new ArrayList<>();

        @Override
        public void error(SAXParseException e) {
            addError(e);
        }

        @Override
        public void fatalError(SAXParseException e) {
            addError(e);
        }

        private void addError(SAXParseException e) {
            String error = e.getMessage();

            int extraMessage = e.getMessage().indexOf("; expected ");
            if (extraMessage > 0) {
                error = e.getMessage().substring(0, extraMessage);
            }

            errors.add(error + " at line " + e.getLineNumber() + ":" + e.getColumnNumber());
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    public static void main(String[] args) throws IOException, SAXNotRecognizedException, SAXNotSupportedException {
        RelaxNGCompactSchemaValidator obj = new RelaxNGCompactSchemaValidator();
        List<String> allErros = obj.validateRnc();
        System.out.println(allErros);
        
    }

    public List<String> validateRnc() throws IOException, SAXNotRecognizedException, SAXNotSupportedException {
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
            // eh.printException(e);
        }

        return eh.getErrors();
    }

}