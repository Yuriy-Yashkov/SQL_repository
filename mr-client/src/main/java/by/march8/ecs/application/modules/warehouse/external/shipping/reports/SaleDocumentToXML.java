package by.march8.ecs.application.modules.warehouse.external.shipping.reports;

import by.march8.entities.warehouse.VSaleDocumentReport;
import by.march8.entities.warehouse.VSaleDocumentsReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;

/**
 * @author tmp on 03.12.2021 11:53
 */
public class SaleDocumentToXML {
    public SaleDocumentToXML(ArrayList<Object> data, String dir, String filename) {
        JAXBContext jaxbContext = null;
        try {
            System.out.println("CREATE XML :" + dir + filename);
            jaxbContext = JAXBContext.newInstance(VSaleDocumentsReport.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            ArrayList<VSaleDocumentReport> list = new ArrayList<VSaleDocumentReport>();
            VSaleDocumentsReport report = new VSaleDocumentsReport();
            report.setEmployees(list);

            for (Object obj : data) {
                VSaleDocumentReport item = (VSaleDocumentReport) obj;
                list.add(item);
            }
            jaxbMarshaller.marshal(report, new File(dir + "REPORT_" + filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
