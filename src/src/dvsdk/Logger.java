package dvsdk;

/**
 *
 * @author develop-dvs
 */
public class Logger {
    public static void put(String str) {
        if (GlobalConfig.DEBUG>0) System.out.println(str);
    }
    public static void pu(String str) {
        if (GlobalConfig.DEBUG>0) System.out.print(str);
    }
    public static void put(Exception ex) {
        if (GlobalConfig.DEBUG>0) System.err.println(ex.getLocalizedMessage());
        if (GlobalConfig.DEBUG>1) ex.printStackTrace(System.err);
    }
    public static void put(String str, Exception ex) {
        if (GlobalConfig.DEBUG>0) System.err.println("["+str+"]: "+ex.getLocalizedMessage());
        if (GlobalConfig.DEBUG>1) ex.printStackTrace(System.err);
    }
}