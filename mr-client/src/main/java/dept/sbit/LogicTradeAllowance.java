/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dept.sbit;

import dept.sbit.model.SubTypeProduct;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class LogicTradeAllowance {

    public HashMap<String, String> getNowTradeAllowance() {
        HashMap<String, String> nowTradeAllowance;// = new HashMap<String, String>();
        DataBaseTradeAllowance dbta = new DataBaseTradeAllowance();
        nowTradeAllowance = dbta.getNowTradeAllowance();
        return nowTradeAllowance;
    }

    public ArrayList<String> getCompanyStore() {
        ArrayList<String> companyStore;// = new ArrayList<String>();
        DataBaseTradeAllowance dbta = new DataBaseTradeAllowance();
        companyStore = dbta.getCompanyStore();
        return companyStore;
    }

    public SubTypeProduct getAllowsByClientId(int clientid) {
        DataBaseTradeAllowance dbta = new DataBaseTradeAllowance();
        SubTypeProduct subTypeProduct = dbta.getAllowanceByClientID(clientid);
        return subTypeProduct;
    }

    public void setAllowsByClientId(int clientid, SubTypeProduct product) {
        DataBaseTradeAllowance dbta = new DataBaseTradeAllowance();
        dbta.setAllowanceByClientId(clientid, product);
    }
}
