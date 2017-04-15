package blog.jsonp.moxy;

import java.util.*;
import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.xml.bind.*;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.oxm.json.*;

public class MarshalDemo {

	public static void main(String[] args) throws Exception {
		// Create the EclipseLink JAXB (MOXy) Marshaller
		Map<String, Object> jaxbProperties = new HashMap<String, Object>(2);
		jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
		jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { Customer.class }, jaxbProperties);
		Marshaller marshaller = jc.createMarshaller();

		// Create the JsonArrayBuilder
		JsonArrayBuilder customersArrayBuilder = Json.createArrayBuilder();

		// Build the First Customer
		Customer customer = new Customer();
		customer.setId(1);
		customer.setFirstName("Jane");
		customer.setLastName(null);

		PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setType("cell");
		phoneNumber.setNumber("555-1111");
		customer.getPhoneNumbers().add(phoneNumber);

		// Marshal the First Customer Object into the JsonArray
		JsonArrayBuilderResult result = new JsonArrayBuilderResult(customersArrayBuilder);
		marshaller.marshal(customer, result);

		// Build List of PhoneNumer Objects for Second Customer
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>(2);

		PhoneNumber workPhone = new PhoneNumber();
		workPhone.setType("work");
		workPhone.setNumber("555-2222");
		phoneNumbers.add(workPhone);

		PhoneNumber homePhone = new PhoneNumber();
		homePhone.setType("home");
		homePhone.setNumber("555-3333");
		phoneNumbers.add(homePhone);

		// Marshal the List of PhoneNumber Objects
		JsonArrayBuilderResult arrayBuilderResult = new JsonArrayBuilderResult();
		marshaller.marshal(phoneNumbers, arrayBuilderResult);

		customersArrayBuilder
				// Use JSR-353 APIs for Second Customer's Data
				.add(Json.createObjectBuilder().add("id", 2).add("firstName", "Bob").addNull("lastName")
						// Included Marshalled PhoneNumber Objects
						.add("phoneNumbers", arrayBuilderResult.getJsonArrayBuilder()))
				.build();

		// Write JSON to System.out
		Map<String, Object> jsonProperties = new HashMap<String, Object>(1);
		jsonProperties.put(JsonGenerator.PRETTY_PRINTING, true);
		JsonWriterFactory writerFactory = Json.createWriterFactory(jsonProperties);
		JsonWriter writer = writerFactory.createWriter(System.out);
		writer.writeArray(customersArrayBuilder.build());
		writer.close();
	}

}