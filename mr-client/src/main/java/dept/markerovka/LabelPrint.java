package dept.markerovka;

import dept.MyReportsModule;
import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vova
 * @date 20.03.2012
 */
public class LabelPrint {
    Map paramLabel;
    Map paramLabelLng;
    String labelPath;

    public LabelPrint(Map paramLabel, Map paramLabelLng, LabelPath lb) {
        this.paramLabel = paramLabel;
        this.paramLabelLng = paramLabelLng;
        labelPath = lb.getPath();
    }

    public void print() {
        try {
            Ole32.CoInitialize();
            DispatchPtr app = new DispatchPtr("NICELabel.Application");
            //app.put("Visible", -1);
            int LabelID = (Integer) app.invoke("LabelOpen", MyReportsModule.progPath + "\\Labels\\" + labelPath);
            //int LabelID = (Integer) app.invoke("LabelOpen", "C:\\8Marta\\Label\\mylabel2.lbl");
            app.invoke("LabelSessionStart", LabelID);

            Object params2[] = new Object[5];
            params2[0] = LabelID;
            params2[1] = "Barcode";
            params2[2] = paramLabel.get("barcode").toString();
            params2[3] = -9999;
            params2[4] = -9999;
            app.invokeN("LabelSetVar", params2);

            Object params3[] = new Object[5];
            params3[0] = LabelID;
            params3[1] = "Naim";
            params3[2] = paramLabelLng.get("naim").toString().toUpperCase();
            params3[3] = -9999;
            params3[4] = -9999;
            app.invokeN("LabelSetVar", params3);

            Object params6[] = new Object[5];
            params6[0] = LabelID;
            params6[1] = "Gost";
            params6[2] = paramLabel.get("gost").toString();
            params6[3] = -9999;
            params6[4] = -9999;
            app.invokeN("LabelSetVar", params6);

            Object params[] = new Object[5];
            params[0] = LabelID;
            params[1] = "Ean";
            String ean;
            if (paramLabel.get("ean").toString().length() > 12) ean = paramLabel.get("ean").toString().substring(0, 12);
            else ean = paramLabel.get("ean").toString();
            params[2] = ean;
            params[3] = -9999;
            params[4] = -9999;
            app.invokeN("LabelSetVar", params);

            Object params5[] = new Object[5];
            params5[0] = LabelID;
            params5[1] = "Fas";
            //DecimalFormat df = new DecimalFormat("0000");
            //String str = String.valueOf(df.format(Integer.parseInt(paramLabel.get("fas").toString())));
            String str = paramLabel.get("fas").toString().trim();
            if (str.length() == 2) str = "00" + str;
            params5[2] = str;
            params5[3] = -9999;
            params5[4] = -9999;
            app.invokeN("LabelSetVar", params5);


            Object params4[] = new Object[5];
            params4[0] = LabelID;
            params4[1] = "Nar";
            params4[2] = paramLabel.get("nar").toString();
            params4[3] = -9999;
            params4[4] = -9999;
            app.invokeN("LabelSetVar", params4);


            Object params7[] = new Object[5];
            params7[0] = LabelID;
            params7[1] = "Rzm";
            params7[2] = paramLabel.get("rzm").toString();
            params7[3] = -9999;
            params7[4] = -9999;
            app.invokeN("LabelSetVar", params7);

            Object params9[] = new Object[5];
            params9[0] = LabelID;
            params9[1] = "Srt";
            params9[2] = paramLabel.get("srt").toString();
            params9[3] = -9999;
            params9[4] = -9999;
            app.invokeN("LabelSetVar", params9);

            Object params8[] = new Object[5];
            params8[0] = LabelID;
            params8[1] = "Sostav1";
            params8[2] = paramLabelLng.get("sostav1").toString();
            params8[3] = -9999;
            params8[4] = -9999;
            app.invokeN("LabelSetVar", params8);


            Object params10[] = new Object[5];
            params10[0] = LabelID;
            params10[1] = "Textil";
            params10[2] = paramLabel.get("textil").toString();
            params10[3] = -9999;
            params10[4] = -9999;
            app.invokeN("LabelSetVar", params10);
              
           /* Object params11[] = new Object[5];
            params11[0] = LabelID;
            params11[1] = "rzm1";
            params11[2] = paramLabel.get("rzm").toString();
            params11[3] = -9999;
            params11[4] = -9999;
            app.invokeN("LabelSetVar", params11);*/

            //------------
            Object params12[] = new Object[5];
            params12[0] = LabelID;
            params12[1] = "LngNar";
            params12[2] = paramLabelLng.get("LngNar").toString();
            params12[3] = -9999;
            params12[4] = -9999;
            app.invokeN("LabelSetVar", params12);

            Object params13[] = new Object[5];
            params13[0] = LabelID;
            params13[1] = "LngFas";
            params13[2] = paramLabelLng.get("LngFas").toString();
            params13[3] = -9999;
            params13[4] = -9999;
            app.invokeN("LabelSetVar", params13);


            Object params14[] = new Object[5];
            params14[0] = LabelID;
            params14[1] = "LngSirio";
            params14[2] = paramLabelLng.get("LngSirio").toString();
            params14[3] = -9999;
            params14[4] = -9999;
            app.invokeN("LabelSetVar", params14);


            Object params15[] = new Object[5];
            params15[0] = LabelID;
            params15[1] = "LngPrice";
            params15[2] = paramLabelLng.get("LngPrice").toString();
            params15[3] = -9999;
            params15[4] = -9999;
            app.invokeN("LabelSetVar", params15);

            Object params16[] = new Object[5];
            params16[0] = LabelID;
            params16[1] = "LngSrt";
            params16[2] = paramLabelLng.get("LngSrt").toString();
            params16[3] = -9999;
            params16[4] = -9999;
            app.invokeN("LabelSetVar", params16);

            Object params17[] = new Object[5];
            params17[0] = LabelID;
            params17[1] = "Sostav2";
            params17[2] = paramLabelLng.get("sostav2").toString();
            params17[3] = -9999;
            params17[4] = -9999;
            app.invokeN("LabelSetVar", params17);

            Object params18[] = new Object[5];
            params18[0] = LabelID;
            params18[1] = "LngAbout";
            params18[2] = "Виробник: ВАТ \"8Марта\" Постачальник ТД\"Промінь\"";
            params18[3] = -9999;
            params18[4] = -9999;
            app.invokeN("LabelSetVar", params18);
            Object params19[] = new Object[5];
            params19[0] = LabelID;
            params19[1] = "LngAbout1";
            params19[2] = "м. Київ пров. Киянівський 3-7 +38(044)4996670";
            params19[3] = -9999;
            params19[4] = -9999;
            app.invokeN("LabelSetVar", params19);

            Object params20[] = new Object[5];
            params20[0] = LabelID;
            params20[1] = "LngRzm";
            params20[2] = paramLabelLng.get("LngRzm").toString();
            params20[3] = -9999;
            params20[4] = -9999;
            app.invokeN("LabelSetVar", params20);

            Object params21[] = new Object[5];
            params21[0] = LabelID;
            params21[1] = "LngSum";
            params21[2] = paramLabelLng.get("LngSum").toString();
            params21[3] = -9999;
            params21[4] = -9999;
            app.invokeN("LabelSetVar", params21);

            Object params22[] = new Object[5];
            params22[0] = LabelID;
            params22[1] = "Kol";
            params22[2] = paramLabel.get("label_kol").toString();
            params22[3] = -9999;
            params22[4] = -9999;
            app.invokeN("LabelSetVar", params22);

            Object params23[] = new Object[5];
            params23[0] = LabelID;
            params23[1] = "LngRzm1";
            params23[2] = paramLabelLng.get("LngRzm1").toString();
            params23[3] = -9999;
            params23[4] = -9999;
            app.invokeN("LabelSetVar", params23);

            Object params24[] = new Object[5];
            params24[0] = LabelID;
            params24[1] = "LngRzm2";
            params24[2] = paramLabelLng.get("LngRzm2").toString();
            params24[3] = -9999;
            params24[4] = -9999;
            app.invokeN("LabelSetVar", params24);

            Object params25[] = new Object[5];
            params25[0] = LabelID;
            params25[1] = "LngRzm3";
            params25[2] = paramLabelLng.get("LngRzm3").toString();
            params25[3] = -9999;
            params25[4] = -9999;
            app.invokeN("LabelSetVar", params25);

            Object params26[] = new Object[5];
            params26[0] = LabelID;
            params26[1] = "rzm1";
            params26[2] = paramLabel.get("rzm1").toString();
            params26[3] = -9999;
            params26[4] = -9999;
            app.invokeN("LabelSetVar", params26);

            Object params27[] = new Object[5];
            params27[0] = LabelID;
            params27[1] = "rzm2";
            params27[2] = paramLabel.get("rzm2").toString();
            params27[3] = -9999;
            params27[4] = -9999;
            app.invokeN("LabelSetVar", params27);

            Object params28[] = new Object[5];
            params28[0] = LabelID;
            params28[1] = "rzm3";
            params28[2] = paramLabel.get("rzm3").toString();
            params28[3] = -9999;
            params28[4] = -9999;
            app.invokeN("LabelSetVar", params28);

            Object params29[] = new Object[5];
            params29[0] = LabelID;
            params29[1] = "LngBrend";
            params29[2] = paramLabelLng.get("LngBrend").toString();
            params29[3] = -9999;
            params29[4] = -9999;
            app.invokeN("LabelSetVar", params29);


            app.invoke("LabelPrint", LabelID, paramLabel.get("label_sum").toString());
            //   System.out.println("Этикетка распечатана" + paramLabel.get("barcode") + " " + paramLabel.get("label_sum"));
            app.invoke("LabelSessionEnd", LabelID);
            app.invoke("Quit");

            Ole32.CoUninitialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
