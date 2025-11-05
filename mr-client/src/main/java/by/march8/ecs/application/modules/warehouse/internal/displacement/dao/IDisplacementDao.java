package by.march8.ecs.application.modules.warehouse.internal.displacement.dao;

import by.march8.entities.warehouse.DisplacementInvoiceView;

import java.util.Date;
import java.util.List;

/**
 * @author Andy 26.07.2016.
 */
public interface IDisplacementDao {

    List<DisplacementInvoiceView> getDisplacementDocumentByPeriodThread(int documentType, Date dateBegin, Date dateEnd);

}
