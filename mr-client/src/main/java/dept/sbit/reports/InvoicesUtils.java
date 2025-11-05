package dept.sbit.reports;

import dept.sbit.reports.model.PropInvoiceBean;

public class InvoicesUtils {

    public static PropInvoiceBean getInvoiceInfo(String documentName) {
        PropInvoiceBean invoice = new PropInvoiceBean();
        invoice.setSeries(documentName.substring(0, 2));
		/*Integer number = 0 ;
		try{
			number = Integer.valueOf(documentName.substring(2,documentName.length())); 
		}catch(Exception e){
			System.out.println("Ошибка парсинга номера накладной для "+documentName);
		}*/
        invoice.setNumber(documentName.substring(2, documentName.length()));
        return invoice;
    }

}
