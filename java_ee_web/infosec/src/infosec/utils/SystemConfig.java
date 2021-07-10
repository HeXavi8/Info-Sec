package infosec.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SystemConfig {
    public static String PRIVATE_KEY="/private.pem";
    public static String PUBLIC_KEY="/public.pem";
    public static String CA_PUBLIC_KEY="/CApublic.pem";
    public static String CA_PRIVATE_KEY="/CAprivate.pem";

    //当前项目位置
    private static String curruntProjectPath="";

    //设置当前项目位置并返回该值
    public static String setAndGetProjectPath() throws IOException {
        File file = new File("");
        curruntProjectPath = file.getCanonicalPath();
        return curruntProjectPath;
    }

}
