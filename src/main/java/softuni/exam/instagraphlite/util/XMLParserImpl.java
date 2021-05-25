package softuni.exam.instagraphlite.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParserImpl implements XMLParser {

    @Override
    @SuppressWarnings("unchecked")
    public <O> O parseXML(Class<O> objectClass, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(objectClass);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (O) unmarshaller.unmarshal(new File(filePath));
    }

    @Override
    public <O> void exportXML(O object, Class<O> objectClass, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(objectClass);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, new File(filePath));
    }
}
