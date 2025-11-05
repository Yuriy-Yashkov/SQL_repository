package dept.tech.innovation;


import by.march8.ecs.framework.common.LogCrutch;
import com.svcon.jdbf.DBFReader;
import workDB.DBF_new;

import java.util.ArrayList;

//import org.apache.log4j.Logger;

/**
 * @author vova
 * @date 19.12.2011
 */
public class ProductionDBF extends DBF_new {

    //private static final Logger log = new Log().getLoger(ProductionDBF.class);
    private static final LogCrutch log = new LogCrutch();

    /**
     * Считывает план выпуска из dbf файла
     *
     * @param path -- путь к dbf
     * @return -- ArrayList(Object[]) (Шифр артикула, рост, размер, кол-во)
     * @throws Exception
     */
    public ArrayList readPlan(String path) throws Exception {
        ArrayList items = new ArrayList();
        try {
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            while (dbfr.hasNextRecord()) {
                obj = dbfr.nextRecord();
                Object[] item = new Object[4];
                if (Integer.parseInt(obj[4].toString().trim()) != 0 && Integer.parseInt(obj[1].toString().trim()) == 737) {
                    item[0] = obj[3];
                    item[1] = obj[4];
                    if (obj[5] != null && !obj[5].toString().trim().equals("")) item[2] = obj[5];
                    else item[2] = 0;
                    item[3] = obj[12];
                    items.add(item);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка чтения дбф плана: ", e);
            System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (dbfr != null) {
                    dbfr.close();
                }
            } catch (Exception e) {
                System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
            }

        }
        return items;
    }

//    public ArrayList readX(String path)throws Exception{
//        ArrayList items = new ArrayList();
//        try {
//                dbfr = new DBFReader(path);
//                Object obj[] = new Object[dbfr.getFieldCount()];
//                while(dbfr.hasNextRecord()){
//                    obj = dbfr.nextRecord();
//                    Object[] item = new Object[4];
//                    if (Integer.parseInt(obj[1].toString().trim()) == 737 & (Integer.parseInt(obj[7].toString().trim()) == 0
//                            | obj[7] == null | obj[7].toString().trim().equals("".toString()))) {
//                        item[0] = obj[3];
//                        NullToZero(obj[4], item[1]);
////                        if(obj[4] != null & !obj[4].toString().trim().equals("".toString())) {
////                           item[1] = obj[4];
////                        }
////                        else {
////                            item[1] = 0;
////                        }
//                        NullToZero(obj[5], item[2]);
////                        if(obj[5] != null & !obj[5].toString().trim().equals("".toString())) {
////                           item[2] = obj[5];
////                        }
////                        else {
////                              item[2] = 0;
////                        }
//                        item[3] = obj[12];
//                        items.add(item);
//                    }
//                }
//            } catch(Exception e) {
//                log.error("Ошибка чтения дбф плана: ", e);
//                System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
//                throw e;
//            } finally {
//                try{
//                    if(dbfr != null) {
//                        dbfr.close();
//                    }
//                } catch(Exception e) {
//                    System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
//                }
//                
//            }
//        return items;
//    }    
//    
//     public ArrayList readXX(String path)throws Exception{
//        ArrayList items = new ArrayList();
//        try {
//                dbfr = new DBFReader(path);
//                Object obj[] = new Object[dbfr.getFieldCount()];
//                while(dbfr.hasNextRecord()){
//                    obj = dbfr.nextRecord();
//                    Object[] item = new Object[6];
//                    if (Integer.parseInt(obj[10].toString().trim()) == 737 & Integer.parseInt(obj[29].toString().trim()) == 3
//                            & Integer.parseInt(obj[15].toString().trim()) != 0 & Integer.parseInt(obj[17].toString().trim()) != 0){
//                        item[0] = obj[15];
//                        item[1] = obj[17];
//                        NullToZero(obj[20], item[2]);
////                        if(obj[20] != null & !obj[20].toString().trim().equals("".toString())) {
////                           item[2] = obj[20];
////                        }
////                        else {
////                          item[2] = 0;
////                        }
//                        NullToZero(obj[21], item[3]);
////                        if(obj[21] != null & !obj[21].toString().trim().equals("".toString())) {
////                           item[3] = obj[21];
////                        }
////                        else {
////                          item[3] = 0;
////                        }
//                        NullToZero(obj[22], item[4]);
////                        if(obj[22] != null & !obj[22].toString().trim().equals("".toString())) {
////                           item[4] = obj[22];
////                        }
////                        else {
////                          item[4] = 0;  
////                        }
//                        NullToZero(obj[23], item[5]);
////                        if(obj[23] != null & !obj[23].toString().trim().equals("".toString())) {
////                          item[5] = obj[23];
////                        }
////                        else {
////                           item[5] = 0;
////                        }
//                        items.add(item);
//                    }
//                }
//            } catch(Exception e) {
//                log.error("Ошибка чтения дбф плана: ", e);
//                System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
//                throw e;
//            } finally {
//                try{
//                    if(dbfr != null) {
//                        dbfr.close();
//                    }
//                } catch(Exception e) {
//                    System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
//                }
//                
//            }
//        return items;
//    }    

//      public ArrayList readXXX(String path)throws Exception{
//        ArrayList items = new ArrayList();
//        try {
//                dbfr = new DBFReader(path);
//                Object obj[] = new Object[dbfr.getFieldCount()];
//                while(dbfr.hasNextRecord()){
//                    obj = dbfr.nextRecord();
//                    Object[] item = new Object[6];
//                    if (Integer.parseInt(obj[2].toString().trim()) == 737 
//                            & Integer.parseInt(obj[5].toString().trim()) != 0 & Integer.parseInt(obj[7].toString().trim()) != 0
//                            & (Integer.parseInt(obj[16].toString().trim()) >= 1539 | Integer.parseInt(obj[16].toString().trim()) <= 1504)) {
//                        item[0] = obj[5];
//                        item[1] = obj[7];
//                        NullToZero(obj[9], item[2]);
////                        item[2] = obj[9];
//                        NullToZero(obj[8], item[3]);
////                        item[3] = obj[8];
//                        NullToZero(obj[12], item[4]);
////                        item[4] = obj[12];
//                        NullToZero(obj[13], item[5]);

    ////                        item[5] = obj[13];
//                        items.add(item);
//                    }
//                }
//            } catch(Exception e) {
//                log.error("Ошибка чтения дбф плана: ", e);
//                System.err.println("Ошибка чтения дбф плана: " + e.getMessage());
//                throw e;
//            } finally {
//                try{
//                    if(dbfr != null) {
//                        dbfr.close();
//                    }
//                } catch(Exception e) {
//                    System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
//                }
//                
//            }
//        return items;
//    }  
    public ArrayList readUnite(String path, ArrayList items, int type) {
        //  ArrayList items = new ArrayList();
        try {
            // plan01.dbf
            dbfr = new DBFReader(path);
            Object obj[] = new Object[dbfr.getFieldCount()];
            if (type == 1) { //26-28
                System.out.println("[1] " + path);
                while (dbfr.hasNextRecord()) {
                    obj = dbfr.nextRecord();
                    Object[] item = new Object[4];
                    if ((Integer.parseInt(obj[1].toString().trim()) == 737
                            | Integer.parseInt(obj[1].toString().trim()) == 738
                            | Integer.parseInt(obj[1].toString().trim()) == 739)
                            & Integer.parseInt(obj[9].toString().trim()) == 1 //VD,N,1,0
                            & Integer.parseInt(NullToZero(obj[3]).toString().trim()) != 0 //ARI,N,8,0
                            & Integer.parseInt(NullToZero(obj[7]).toString().trim()) == 0
                            & Float.parseFloat(NullToZero(obj[12]).toString().trim()) != 0) {
                        item[0] = obj[3];
                        item[1] = NullToZero(obj[4]);
                        item[2] = NullToZero(obj[5]);
                        item[3] = obj[12];
                        items.add(item);

                    }
                }
            } else if (type == 2) {
                // pr_
                System.out.println("[2] " + path);
                while (dbfr.hasNextRecord()) {
                    obj = dbfr.nextRecord();
                    Object[] item = new Object[6];
                    if ((Integer.parseInt(obj[10].toString().trim()) == 737 //POLN,N,4,0
                            | Integer.parseInt(obj[10].toString().trim()) == 738
                            | Integer.parseInt(obj[10].toString().trim()) == 739)
                            & Integer.parseInt(obj[29].toString().trim()) == 3 //PS,N,1,0
                            & Integer.parseInt(obj[15].toString().trim()) != 0
                            & Integer.parseInt(obj[17].toString().trim()) != 0
                            & Float.parseFloat(obj[22].toString().trim()) != 0
                            & Float.parseFloat(obj[23].toString().trim()) != 0) {
                        item[0] = obj[15];
                        item[1] = obj[17];
                        item[2] = NullToZero(obj[20]);
                        item[3] = NullToZero(obj[21]);
                        if (Integer.parseInt(obj[4].toString().trim()) == 38) {
                            item[4] = -Float.parseFloat(obj[22].toString().trim());
                        } else {
                            item[4] = obj[22];
                        }
                        item[5] = obj[23];
                        items.add(item);
                    }
                }
            } else if (type == 3) {
                System.out.println("[3] " + path);
                while (dbfr.hasNextRecord()) {
                    obj = dbfr.nextRecord();
                    Object[] item = new Object[6];
                    if ((Integer.parseInt(obj[2].toString().trim()) == 737
                            | Integer.parseInt(obj[2].toString().trim()) == 738
                            | Integer.parseInt(obj[2].toString().trim()) == 739)
                            & Integer.parseInt(obj[5].toString().trim()) != 0
                            & Integer.parseInt(obj[7].toString().trim()) != 0
                            & Float.parseFloat(obj[12].toString().trim()) != 0
                            & Float.parseFloat(obj[13].toString().trim()) != 0
                            & (Integer.parseInt(obj[16].toString().trim()) >= 1539
                            | Integer.parseInt(obj[16].toString().trim()) <= 1504)) {
                        item[0] = obj[5];
                        item[1] = obj[7];
                        item[2] = NullToZero(obj[9]);
                        item[3] = NullToZero(obj[8]);
                        item[4] = obj[12];
                        item[5] = obj[13];
                        items.add(item);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dbfr != null) {
                    dbfr.close();
                }
            } catch (Exception e) {
                System.err.println("Ошибка закрытия дбф плана: " + e.getMessage());
            }
        }
        return items;
    }

    public Object NullToZero(Object o) {
        if (o == null & o.toString().trim().equals("")) {
            o = 0;
        }
        return o;
    }
}
