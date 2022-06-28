package dvsdk.rest;

import dvsdk.GlobalConfig;
import dvsdk.Logger;
import dvsdk.client.MulisTable;
import dvsdk.form.IInternal;
import dvsdk.util.UtilSQL;
import dvsdk.util.Util;

/**
 *
 * @author develop-dvs
 */
public class InternallWorker implements Runnable {

    private Thread thread;
    //private IFrameCore core;
    private IInternal core_int;
    private int cur_i=0,cur_j=0;
    private String from_id="", to_id="";

    public InternallWorker(IInternal core_int, String from_id, String to_id) {
        //this.core = core;
        this.core_int = core_int;
        int cur_t=GlobalConfig.getCore().getCurrentIntFrame();
        this.cur_i=cur_t/10%10-1;
        this.cur_j=cur_t/10-1;
        this.from_id=from_id;
        this.to_id=to_id;
        init();
    }

    private void init() {
        thread = new Thread(this);
        thread.setName("InternalWorker");
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
        }
    }

    @Override
    public void run() {
        try {
            int i=0; int ar[];
            core_int.showProgressBas(Util.F_RENT_ARR.length+Util.F_SALE_ARR.length+Util.F_OUTLAND_ARR.length+Util.F_BUY_ARR.length); //+Util.F_BUY_ARR.length
            while (i<=1) {
                switch (i) {
                    case 0: default: ar=Util.F_SALE_ARR; break;
                    case 1: ar=Util.F_RENT_ARR; break;
                    case 2: ar=Util.F_OUTLAND_ARR; break;
                    case 3: ar=Util.F_BUY_ARR; break;
                }
                for (int num : ar) {
                    GlobalConfig.getCore().setCurrentTab(i, num);
                    MulisTable table = GlobalConfig.getCore().getCurrentTable();
                    UtilSQL.UpdIDRElements(GlobalConfig.getCore().getBase(table.getName()), table.getName(), from_id, to_id);
                    //Util.massMoveImagesDir(to_id, from_id, GlobalConfig.MASTER_INN, table.getName());
                    GlobalConfig.getCore().refreshCurrentTable(true);
                    core_int.incProgressBas();
                }
                i++;
            }

            GlobalConfig.getCore().setCurrentTab(cur_i, cur_j);
            core_int.hideProgressBas();
        } catch (Exception ex) {
            core_int.hideProgressBas();
            Logger.put(ex);
        }
    }
}