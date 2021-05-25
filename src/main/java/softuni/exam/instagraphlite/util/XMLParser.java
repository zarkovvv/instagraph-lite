package softuni.exam.instagraphlite.util;

import javax.xml.bind.JAXBException;

public interface XMLParser {

    <O> O parseXML(Class<O> objectClass ,String filePath) throws JAXBException;
    <O> void exportXML(O object, Class<O> objectClass, String filePath) throws JAXBException;
}
