package xsd.contractor;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the xsd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: xsd
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ContractorXML }
     *
     */
    public ContractorXML createContractorXML() {
        return new ContractorXML();
    }

    /**
     * Create an instance of {@link ContractorMarch8 }
     *
     */
    public ContractorMarch8 createContractorMarch8() {
        return new ContractorMarch8();
    }

    /**
     * Create an instance of {@link AddressMarch8 }
     *
     */
    public AddressMarch8 createAddressMarch8() {
        return new AddressMarch8();
    }

    /**
     * Create an instance of {@link EmailMarch8 }
     *
     */
    public EmailMarch8 createEmailMarch8() {
        return new EmailMarch8();
    }

    /**
     * Create an instance of {@link ContractMarch8 }
     *
     */
    public ContractMarch8 createContractMarch8() {
        return new ContractMarch8();
    }

}
