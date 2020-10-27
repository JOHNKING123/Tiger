package com.zhengcq.srv.client1.srv.client1;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.File;

/**
 * @ClassName: TestFile
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/8/17
 */
public class TestFile {

    @Test
    public void test1() {
        String filePath = "D:\\Documents\\Downloads\\Compressed\\zhidao_qa.json";
        LineIterator it = null;

        try {
            File file = new File(filePath);
           it = FileUtils.lineIterator(file, "UTF-8");
           int i = 0;
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
                i++;
                if (i % 100000 == 0) {
                    System.out.println(line);
                    System.out.println(i);
                }
            }
            System.out.println(i);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
