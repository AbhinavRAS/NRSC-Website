package ehiring.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ehiring.dao.LogManager;

public class ZipDirectory {

//	public static void main(String[] args) throws IOException {
//		File directoryToZip = new File("F:\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\eRecruitment_NRSC\\Recruitment_data\\applicant\\saikalpana_t_nrsc_gov_in\\abcd12345\\1");
//		ZipDirectory z  =  new ZipDirectory();
//
//		List<File> fileList = new ArrayList<File>();
//		logMgr.accessLog("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
//		z.getAllFiles(directoryToZip, fileList);
//		logMgr.accessLog("---Creating zip file");
//		z.writeZipFile(directoryToZip,"please", fileList);
//		logMgr.accessLog("---Done");
//	}
	
	private static LogManager logMgr;

	public 	ZipDirectory(String email)
	{
		logMgr = LogManager.getInstance(email);
	}
	public boolean getAllFiles(File dir, List<File> fileList, String email) {
		try {
			logMgr.accessLog("List of files in zip. 123............:" + dir.toString());
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.getName().indexOf(email) >= 0) {
					fileList.add(file);
					if (file.isDirectory()) {
						logMgr.accessLog("directory:" + file.getCanonicalPath());
						getAllFiles(file, fileList, email);
					} else {
						logMgr.accessLog("     file:" + file.getCanonicalPath());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String writeZipFile(File directoryToZip, String zipName, List<File> fileList) {

		try {
			FileOutputStream fos = new FileOutputStream(directoryToZip + File.separator + zipName + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			logMgr.accessLog("fileList length is :" + fileList.toString());

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Files Not Found";
		} catch (IOException e) {
			e.printStackTrace();
			return "Could not Zip Files";
		}
		return "success";
	}

	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);

		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().indexOf("admin") + 10,
				file.getCanonicalPath().length());
		// logMgr.accessLog("directoryToZip .....'" +
		// directoryToZip.getCanonicalPath().length() + "'
		// directoryToZip.getCanonicalPath():"+directoryToZip.getCanonicalPath().toString());
		// logMgr.accessLog("file .....'" + file.getCanonicalPath().length() + "'
		// file.getCanonicalPath():"+file.getCanonicalPath().toString());
		logMgr.accessLog("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

}