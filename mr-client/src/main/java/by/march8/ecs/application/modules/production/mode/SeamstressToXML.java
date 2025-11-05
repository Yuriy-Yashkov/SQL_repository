package by.march8.ecs.application.modules.production.mode;

import by.march8.entities.seamstress.ViewSeamstressItem;
import by.march8.entities.seamstress.ViewSeamstressReport;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andy on 27.12.2021 12:01
 */
public class SeamstressToXML {

    public SeamstressToXML(List<ViewSeamstressItem> data, String dir, String filename) {
        JAXBContext jaxbContext = null;
        try {

            jaxbContext = JAXBContext.newInstance(ViewSeamstressReport.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            ArrayList<ViewSeamstressItem> list = new ArrayList<ViewSeamstressItem>();
            ViewSeamstressReport report = new ViewSeamstressReport();
            report.setEmployees(list);

            for (Object obj : data) {
                ViewSeamstressItem item = (ViewSeamstressItem) obj;
                list.add(item);
            }
            jaxbMarshaller.marshal(report, new File(dir + "REPORT_" + filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
