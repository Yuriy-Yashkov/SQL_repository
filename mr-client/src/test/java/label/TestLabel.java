package label;

import by.march8.ecs.framework.sdk.hardware.NiceLabel;
import com.jacob.com.Variant;
import org.junit.Ignore;
import org.junit.Test;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class TestLabel {

    @Test
    @Ignore
    public void testLabel() {

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);

        for (PrintService printer : printServices)
            System.out.println("Printer: " + printer.getName());


        NiceLabel label = new NiceLabel();
        Variant id = label.openLabel("c:\\2predm.lbl");
        label.setPrinter(id, "FinePrint");
        label.printLabel(id, 1);
        label.closeLabel(id);


        //Variant id = label.openLabel("c:\\Label1.lbl");
        //label.createImage("c:\\textile2.lbl","c:\\test_label.jpg", new Dimension(400,400));
        //label.closeLabel(id);

/*
        try {
            Dimension size = new Dimension(400,400);
            List<File> list = new ArrayList<>();
            Files.walk(Paths.get("c:\\labels\\"))
                    .filter(Files::isRegularFile)
                    .forEach(a -> {
                        String path = a.toString();
                        if (path.toLowerCase().endsWith(".lbl")) {
                            list.add(new File(path));
                        }
                    });

            for(File path: list){
                System.out.println(path.getAbsoluteFile());
                label.createImage(path.getAbsolutePath(),"C:\\out\\"+path.getName()+".jpg",size );
                //label.getLabelVariables(path.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
*/


    }


}
