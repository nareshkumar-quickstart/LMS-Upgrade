package com.softech.vu360.lms.web.controller.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.instructor.AddCourseController;

public class FileUploadUtils {

    static final int BUFFER = 2048;
    
    static final String ALL_EXTENSIONS = "*";
    
    private static Logger log = Logger.getLogger(FileUploadUtils.class.getName());

    // this method will copy the multipart file to the temporary directory.
    public static String copyFile(FileItem soursceFile, File destinationFile) throws Exception {
	// soursceFile.transferTo(destinationFile);
	soursceFile.write(destinationFile);
	return destinationFile.getCanonicalPath();
    }

    @SuppressWarnings("unused")
    private static void copyFile(String srFile, String dtFile) throws IOException {

	File f1 = new File(srFile);
	File f2 = new File(dtFile);
	InputStream in = new FileInputStream(f1);

	OutputStream out = new FileOutputStream(f2);
	byte[] buf = new byte[1024];
	int len;
	while ((len = in.read(buf)) > 0) {
	    out.write(buf, 0, len);
	}
	in.close();
	out.close();
	log.debug("File copied.");
    }

    @SuppressWarnings("unused")
    public static String copyFile(MultipartFile srFile, File dtFile) throws IOException {

	dtFile.createNewFile();
	srFile.transferTo(dtFile);
	log.info("File copied. SUCCESSFULLY : " + dtFile.getCanonicalPath());
	return dtFile.getCanonicalPath();

    }

    public static File getXMLFileFromZIP(String fileName) throws IOException {

	ZipFile zip = new ZipFile(new File(fileName));
	File xmlFile = null;
	for (Enumeration e = zip.entries(); e.hasMoreElements();) {
	    ZipEntry entry = (ZipEntry) e.nextElement();
	    log.debug("File name: " + entry.getName() + "; size: " + entry.getSize() + "; compressed size: " + entry.getCompressedSize());
	    if (entry.getName().contains("imsmanifest.xml"))// its generic....
	    {
		log.debug("THIS ZIP CONTAINS THE REQUIRED FILE :: " + entry.getName());
		InputStream is = zip.getInputStream(entry);
		InputStreamReader isr = new InputStreamReader(is);
		xmlFile = new File("imsmanifest.xml");
		OutputStream os = new FileOutputStream(xmlFile);

		OutputStream out = new FileOutputStream(xmlFile);
		// Transfer bytes from the ZIP file to the output file
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
		    out.write(buf, 0, len);
		}
		// Close the streams
		out.close();
		is.close();

		// no need to iterate firthur... break here...
		break;

	    }// IF FILE FOUND end...
	}
	zip.close();
	return xmlFile;

    }

    public static void extractZIPFile(String zipFileName, String destinationPath) throws IOException {

	java.util.jar.JarFile zipFile = new java.util.jar.JarFile(zipFileName);
	// Enumeration enm = zipFile.entries();
	File temp = new File(destinationPath);
	if (!temp.exists()) {
	    log.debug("DIRECTOPRY Created.." + temp.getCanonicalPath());
	    temp.mkdirs();
	}

	JarInputStream in = new JarInputStream(new FileInputStream(zipFileName));
	JarFile zf = new JarFile(zipFileName);
	JarEntry file = new JarEntry(zipFileName);

	extractDirectories(in, zf, destinationPath);

	java.io.InputStream is = zipFile.getInputStream(file);

	extractFiles(is, zf, destinationPath);

	in.close();

    }

    /**
     * @param out
     * @param in
     * @param zf
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static boolean extractDirectories(ZipInputStream in, ZipFile zf, String parentPath) throws FileNotFoundException, IOException {

	int a = 0;
	File file;
	File dir;
	Enumeration em = zf.entries();

	while (em.hasMoreElements()) {
	    String targetfile = em.nextElement().toString();
	    targetfile = parentPath + File.separator + targetfile;

	    file = new File(targetfile);

	    if (!file.isDirectory()) {
		dir = new File(file.getParent());

		// create zip entry parent folder if not exists
		if (!(dir.exists())) {

		    (new File(file.getParent())).mkdir();
		}

	    } else {
		if (!(file.exists())) {
		    if (!(new File(file.getParent()).exists())) {

			(new File(file.getParent())).mkdir();
		    }

		    file.mkdir();
		}

	    }
	    a += 1;
	}

	if (a > 0)
	    System.out.println("Folders unzipped.");

	return true;
    }

    private static void extractFiles(java.io.InputStream in, ZipFile zf, String parentPath) throws FileNotFoundException, IOException {

	int a = 0;
	byte[] buf;
	int len;
	File file;

	Enumeration em = zf.entries();
	OutputStream out = null;

	while (em.hasMoreElements()) {
	    java.util.jar.JarEntry zipFile = (JarEntry) em.nextElement();
	    String targetfile = zipFile.toString();
	    targetfile = parentPath + File.separator + targetfile;

	    in = zf.getInputStream(zipFile);

	    file = new File(targetfile);
	    log.debug("Extracting file: " + targetfile);

	    if (!file.isDirectory()) {
		// create zip entry file
		out = new FileOutputStream(targetfile);
		buf = new byte[2048];

		while ((len = in.read(buf)) > 0) {
		    out.write(buf, 0, len);

		}

		out.flush();
		out.close();

	    }
	    a += 1;
	}

	if (a > 0)
	    log.debug("Files unzipped.");

    }

    public static boolean deleteDir(File dir) {
	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    return false;
		}
	    }
	}

	// The directory is now empty so delete it
	return dir.delete();
    }

    public static boolean copy(String fromFileName, String toFileName) throws IOException {
	File fromFile = new File(fromFileName);
	File toFile = new File(toFileName);

	if (!fromFile.exists())
	    throw new IOException("FileCopy: " + "no such source file: " + fromFileName);
	if (!fromFile.isFile())
	    throw new IOException("FileCopy: " + "can't copy directory: " + fromFileName);
	if (!fromFile.canRead())
	    throw new IOException("FileCopy: " + "source file is unreadable: " + fromFileName);

	if (toFile.isDirectory())
	    toFile = new File(toFile, fromFile.getName());

	if (toFile.exists()) {
	    if (!toFile.canWrite())
		throw new IOException("FileCopy: " + "destination file is unwriteable: " + toFileName);
	    /*
	     * [06/10/2010] For VCS-267 :: Avatar Uploading ** Commenting out -
	     * as we'll always override the avatar without asking user.
	     * System.out.print("Overwrite existing file " + toFile.getName()+
	     * "? (Y/N): "); System.out.flush(); BufferedReader in = new
	     * BufferedReader(new InputStreamReader(System.in)); String response
	     * = in.readLine(); if (!response.equals("Y") &&
	     * !response.equals("y")) throw new IOException("FileCopy: " +
	     * "existing file was not overwritten.");
	     */
	} else {
	    String parent = toFile.getParent();
	    if (parent == null)
		parent = System.getProperty("user.dir");
	    File dir = new File(parent);
	    if (!dir.exists())
		throw new IOException("FileCopy: " + "destination directory doesn't exist: " + parent);
	    if (dir.isFile())
		throw new IOException("FileCopy: " + "destination is not a directory: " + parent);
	    if (!dir.canWrite())
		throw new IOException("FileCopy: " + "destination directory is unwriteable: " + parent);
	}

	FileInputStream from = null;
	FileOutputStream to = null;
	try {
	    from = new FileInputStream(fromFile);
	    to = new FileOutputStream(toFile);
	    byte[] buffer = new byte[4096];
	    int bytesRead;

	    while ((bytesRead = from.read(buffer)) != -1)
		to.write(buffer, 0, bytesRead); // write
	} finally {
	    if (from != null)
		try {
		    from.close();
		} catch (IOException e) {
		    e.printStackTrace();
		    return false;
		}
	    if (to != null)
		try {
		    to.close();
		} catch (IOException e) {
		    e.printStackTrace();
		    return false;
		}
	}
	return true;
    }

    /**
     * 
     * @param fileNameWithPath
     * @param response
     */
    public static boolean downloadFile(String fileNameWithPath, HttpServletResponse response) {

	log.debug("filePath " + fileNameWithPath);
	boolean isSuccess = true;
	File file = null;
	FileInputStream inputStream = null;
	try {
	    file = new File(fileNameWithPath);

	    inputStream = new FileInputStream(file);
	    log.debug(" inputStream " + inputStream);
	    byte[] bytes;
	    bytes = new byte[inputStream.available()];
	    inputStream.read(bytes);
	    inputStream.close();
	    response.setContentType("application/octet-stream");
	    response.setContentLength(bytes.length);
	    response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
	    ServletOutputStream ouputStream = response.getOutputStream();

	    ouputStream.write(bytes, 0, bytes.length);
	    ouputStream.flush();
	    ouputStream.close();

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    isSuccess = false;
	    log.error(e.getMessage());

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    isSuccess = false;
	    log.error(e.getMessage());
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	return isSuccess;
    }

    /*
     * Create a zip file which comprises the files in the passing parameter Zip
     * file name is placed at the 0 index of files param
     */
    public static void downloadZipFile(String[] files) {

	ZipOutputStream zos = null;
	FileInputStream fis = null;
	byte[] buffer = new byte[1024];
	String zipFilename = files[0];
	int bytesRead;

	try {

	    zos = new ZipOutputStream(new FileOutputStream(zipFilename));

	    for (int i = 1; i < files.length; i++) {

		try {
		    fis = new FileInputStream(new File(files[i]));

		    zos.putNextEntry(new ZipEntry(new File(files[i]).getName()));
		    while ((bytesRead = fis.read(buffer)) > 0) {
			zos.write(buffer, 0, bytesRead);
		    }
		    zos.closeEntry();
		    fis.close();
		} catch (FileNotFoundException fnfe) {
		    log.error("System didn't find the file ..." + files[i]);
		    log.error(fnfe.getMessage());
		} catch (Exception e) {
		    log.error("Error occured during the archiving of file ..." + files[i]);
		    log.error(e.getMessage());
		} finally {
		    if (fis != null) {
			try {
			    fis.close();
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }
		}

	    }// end of For loop
	    zos.close();

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (zos != null) {
		try {
		    zos.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    public static boolean validFileFormate(String fileName, String fileExts) {

	log.debug("validFileFormate(" + fileName + "," + fileExts + ")");
	
	// if configured extension is * it means all kind of file ext are allowed
	if( !StringUtils.isBlank(fileExts) &&  ALL_EXTENSIONS.equals(fileExts)) return true;
	
	String[] fileFormates = fileExts.split(",");
	
	boolean correctFile = false;
	for (String supportedExt : fileFormates) {
	    if (fileName.toLowerCase().indexOf(supportedExt) > -1) {
		correctFile = true;
	    }
	}
	return correctFile;
    }

    public static Document uploadFile(MultipartFile originalFile, String fileLocation, ContentOwner contentOwner) {
	Document document = null;
	String fileName = System.currentTimeMillis() + "_" + originalFile.getOriginalFilename();
	String filePath = fileLocation + File.separator + fileName;
	File tempFile = new File(filePath);
	if (!tempFile.exists()) {
	    try {
		copyFile(originalFile, tempFile);
		document = new Document();
		document.setFileName(fileName);
		document.setContentowner(contentOwner);
		document.setName(AddCourseController.HOMEWORK_ASSIGNMENT);
		AddCourseController.log.info("FILE UPLOADED............. . " + fileName);
	    } catch (IOException e) {
		AddCourseController.log.error(e);
	    }
	}
	// return fileName;
	return document;
    }
}// class end