package dvsdk.rest;

import dvsdkweb.usr.BeanTransSettings;

/**
 *
 * @author develop-dvs
 */
public interface IWorkerMsg {
    public void msg(String str);
    public void parseAgent(BeanTransSettings bts);
}