package dvsdk.rest;

import com.google.gson.Gson;
import dvsdk.GlobalConfig;
import dvsdk.util.Lang;
import dvsdkweb.usr.BeanTransSettings;

/**
 *
 * @author develop-dvs
 */
public class ExternalWorkerSettings implements Runnable {

    private Thread thread;
    private IWorkerMsg workerMsg;

    public ExternalWorkerSettings(IWorkerMsg workerMsg) {
        this.workerMsg=workerMsg;
        init();
    }
    
    private void init() {
        thread = new Thread(this);
        thread.setName("ExternalWorkerSettings");
        thread.start();
    }

    @Override
    public void run() {
        try {
            NewJerseyClient client = new NewJerseyClient(GlobalConfig.GLOBAL_MULIS_SERVER_PATH_SETTINGS);
            client.setUsernamePassword(GlobalConfig.MASTER_INN_GLOBAL, GlobalConfig.MASTER_PWD_MD5);
            
            Gson gson = new Gson();
            BeanTransSettings bts = gson.fromJson(client.getState(), BeanTransSettings.class);    
            //BeanTransAgent bta = bts.getBta();
            //if (bts.getBta().size()>0) {
            workerMsg.parseAgent(bts);
            //}
            //workerMsg.msg("Проверка подключения к серверу успешно завершена.");
        } catch (Exception ex) {
            workerMsg.msg(Lang.decodeErrorServerAnswer(ex));
        }
    }
}