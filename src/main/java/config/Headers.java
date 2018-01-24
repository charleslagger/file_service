package config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Headers {
    //set header
    public static String epNAME;
    public static String epMac;
    public static String epAuth;
    public static String clientSession;
    public static String clientDevice;
    public static String clientIp;
    public static String userName;
    public static String contentType;

    public Headers(Environment env) {
        epNAME = env.getProperty("EP_NAME");
        epMac = env.getProperty("EP_MAC");
        epAuth = env.getProperty("EP_AUTH");
        clientSession = env.getProperty("Client_Session");
        clientDevice = env.getProperty("Client_Device");
        clientIp = env.getProperty("Client_Ip");
        userName = env.getProperty("User_Name");
        contentType = env.getProperty("Content-Type");
    }
}
