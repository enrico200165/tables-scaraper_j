package blog.jsonp.moxy;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.oxm.json.JsonStructureSource;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnmarshalDemo {

    public static void main(String[] args) throws Exception {
    	System.err.print(new java.io.File( "." ).getCanonicalPath());
        try (FileInputStream is = new FileInputStream("src/input.json")) {
            // Create the EclipseLink JAXB (MOXy) Unmarshaller
            Map<String, Object> jaxbProperties = new HashMap<String, Object>(2);
            jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
            jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
            JAXBContext jc = JAXBContext.newInstance(new Class[] {Customer.class}, 
                jaxbProperties);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            // Parse the JSON
            JsonReader jsonReader = Json.createReader(is);

            // Unmarshal Root Level JsonArray
            JsonArray customersArray = jsonReader.readArray();
            JsonStructureSource arraySource = new JsonStructureSource(customersArray);
            List<Customer> customers = 
                (List<Customer>) unmarshaller.unmarshal(arraySource, Customer.class)
                .getValue();
            for(Customer customer : customers) {
                System.out.println(customer.getFirstName());
            }

            // Unmarshal Nested JsonObject
            JsonObject customerObject = customersArray.getJsonObject(1);
            JsonStructureSource objectSource = new JsonStructureSource(customerObject);
            Customer customer = unmarshaller.unmarshal(objectSource, Customer.class)
                .getValue();
            for(PhoneNumber phoneNumber : customer.getPhoneNumbers()) {
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

}