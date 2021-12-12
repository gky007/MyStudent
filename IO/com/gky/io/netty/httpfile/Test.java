package com.gky.io.netty.httpfile;

import com.gky.io.netty.utils.HttpCallerUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:41
 **/
public class Test {
    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap();
        byte[] ret = HttpCallerUtils.getStream("http://127.0.0.1:8765/sources/a.doc", params);
        String writePath = System.getProperty("user.dir") + File.separatorChar + "receive" + File.separatorChar + "a.doc";
        File file = new File(writePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(writePath);
        fos.write(ret);
        fos.close();
    }
}
