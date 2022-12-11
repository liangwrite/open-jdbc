package cn.fomer.common.util;

/**
 * 202201
 */
public class Caller {
    public static Class getParent() {
        Class clazz= new java.lang.SecurityManager()
        {
            public Class getCallerClass()
            {
                Class[] class_array= getClassContext(); //["com.test.Main$1","com.test.Main"]
                String log_text= "";
                for(Integer i=0;i<class_array.length;i++)
                {
                    Class class1= class_array[i];
                    log_text+= i+"\t"+class1.getName()+"\r\n";

                }
                // System.out.println(log_text);
                return class_array[class_array.length-1]; //最后一个
            }
        }.getCallerClass();

        return clazz;
    }
}
