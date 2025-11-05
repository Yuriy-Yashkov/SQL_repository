package by.march8.ecs.application.modules.warehouse.internal.displacement.reports;

import by.march8.entities.warehouse.VInternalInvoiceReport;
import by.march8.entities.warehouse.VInternalInvoicesReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;

/**
 * @author tmp on 03.12.2021 13:36
 */
public class InternalInvoiceToXML {

    public InternalInvoiceToXML(ArrayList<Object> data, String dir, String filename) {
        JAXBContext jaxbContext = null;
        try {
            System.out.println("CREATE XML :" + dir + filename);
            jaxbContext = JAXBContext.newInstance(VInternalInvoicesReport.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            ArrayList<VInternalInvoiceReport> list = new ArrayList<VInternalInvoiceReport>();
            VInternalInvoicesReport report = new VInternalInvoicesReport();
            report.setItems(list);

            for (Object obj : data) {
                VInternalInvoiceReport item = (VInternalInvoiceReport) obj;
                list.add(item);
            }
            jaxbMarshaller.marshal(report, new File(dir + "REPORT_" + filename));
            System.out.println(dir);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
