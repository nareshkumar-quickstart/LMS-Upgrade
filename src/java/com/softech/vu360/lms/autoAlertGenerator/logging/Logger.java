package com.softech.vu360.lms.autoAlertGenerator.logging;

import org.apache.log4j.PropertyConfigurator;

///**
// *
// * Logger provides a simple text logging
// * The log file will be generated on the
// * root of the main caller.
// *
// * <p>
// * The log has a special rule when the size
// * of the log gets bigger than 10 MB it will
// * be flushed and an new log will be created
// *
// * @author ramiz.uddin
// * @version 0.1 10/16/12 3:38
// */
//public class Logger {
//
//    static  String logFileName = "AlertService.log";
//
//	/*
//	* Append message in a text file
//	*
//	* @param message String
//	*/
//    public static void write(String message) {
//
//        try {
//            flush();
//            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)));
//            out.println();
//            out.println((new Date()));
//            out.println("=======");
//            out.println(message);
//            out.close();
//        } catch (IOException e) {
//            //oh noes!
//        }
//    }
//
//	/*
//	*
//	* Delete log file when it gets
//	* to 10 MB file size
//	*
//	*/
//    private static void flush() {
//        File file = new File(logFileName);
//        try{
//            String fileSize = logSize(file.length());
//            String[] fileInfo = fileSize.split(" ");
//
//            int length;
//
//            if(fileInfo.length == 2) {
//                if(fileInfo[1].equalsIgnoreCase("MB")) {
//                    length = (int)Double.parseDouble(fileInfo[0]);
//
//                    if(length > 10)
//                        file.delete();
//                }
//            }
//
//        } finally { }
//    }
//
//	/*
//	* It returns size as text which includes
//	* unit (B, KB, MB, GB, TB)
//	*
//	* http://stackoverflow.com/a/5599842/134743
//	*
//	* @param size Long
//	*/
//    private static String logSize(long size) {
//        if(size <= 0) return "0";
//        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
//        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
//        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
//    }
//
//
//}


/*
*
* Logging service for
* AlertQueuingProcess
* *
* @author ramiz.uddin
* @version 0.1
* */
public class Logger {

    private final static org.apache.log4j.Logger log4j;

    /*
    *
    * Log4j configuration for
    * AlertQueuingProcess service
    *
    * */
    static {
        //final String fileName = "Logger.class";
        //String directory = Logger.class.getResource(fileName).getPath();
        //directory = directory.replace(fileName, "");
        //File propertiesFile = new File(directory, "log4j.properties");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        PropertyConfigurator.configure(classLoader.getResource("log4j.properties"));
        //PropertyConfigurator.configure(propertiesFile.toString());
        log4j =  org.apache.log4j.Logger.getLogger(Logger.class);
    }
    
    public static void write(String message) {

        log4j.info(message);

    }

}