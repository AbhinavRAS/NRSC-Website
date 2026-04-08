/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.operation;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.properties.LoadProperties;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecruitmentDirectoryOperation {
	public String dirpath = new String();
	public HashMap mp = new HashMap();
	public String contextPath = new String();
	LogManager logMgr;
	
	public RecruitmentDirectoryOperation(String emailLogId) {
		logMgr = LogManager.getInstance(emailLogId);
		
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getDirpath() {
		return dirpath;
	}

	public HashMap getAppDirPath() {
		return mp;
	}

	public void setDirpath(String dirpath) {
		this.dirpath = dirpath;
	}

	public static void main(String args[]) {
		RecruitmentDirectoryOperation md = new RecruitmentDirectoryOperation("emailLogId");
		AdvertisementOperation advOp = new AdvertisementOperation("emailLogId");
		String advtId = new String();
		try {
			advtId = advOp.generateAdvtID();
		} catch (SQLException ex) {
			Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.SEVERE, null, ex);
		}
		md.createAppIdFolders(advtId);
	}

	/*
	 * public boolean createAppIdFolders(String emailId) { LoadProperties lp = new
	 * LoadProperties(); try { String[] emailUserName = emailId.split("@");
	 * logMgr.accessLog("AFTER SPLIT....:"+emailUserName[0]); String parentDirName
	 * = lp.getProperty("RECRUIT_FOLDER_DIR"); logMgr.accessLog("parentDirName : "
	 * + parentDirName);
	 * 
	 * String subDirNames = lp.getProperty("RECRUIT_FOLDER"); StringTokenizer st =
	 * new StringTokenizer(subDirNames, ", "); String tokenName = st.nextToken();
	 * while (st.hasMoreTokens()) { String subDirName =
	 * contextPath+File.separator+parentDirName + File.separator + emailUserName[0]
	 * + File.separator + tokenName; File subDir = new File(subDirName); if
	 * (!subDir.exists()) {
	 * 
	 * subDir.setExecutable(true, false); subDir.setReadable(true, false);
	 * subDir.setWritable(true, false); subDir.mkdirs(); }
	 * mp.put(tokenName,subDirName);
	 * 
	 * } logMgr.accessLog("hashmap:"+mp.toString()); } catch (Exception ex) {
	 * Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.
	 * SEVERE, null, ex); } return true; }
	 */

	public String getAppIdFolderName(String context, String emailId) {
		//String appIdFolder = context + File.separator;
		String appIdFolder = new String();
		LoadProperties lp = new LoadProperties();
		try {
			//String[] emailUserName = emailId.split("@");
		
			String emailUserName = emailId.replace("@","_");
		//	logMgr.accessLog("AFTER SPLIT......1:" + emailUserName);
			emailUserName = emailUserName.replace(".","_");
			logMgr.accessLog("AFTER SPLIT......2:" + emailUserName);
			String parentDirName = lp.getProperty("RECRUIT_FOLDER_DIR");
			logMgr.accessLog("parentDirName : " + parentDirName);
		//	appIdFolder += parentDirName + File.separator + emailUserName[0] + File.separator;
			appIdFolder += parentDirName + File.separator + emailUserName + File.separator;
			logMgr.accessLog("appIdFolder : " + appIdFolder);
		} catch (Exception ex) {
			Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.SEVERE, null, ex);
		}
		return appIdFolder;

	}

	public String getLogo(String context) {
		String appIdFolder = context + File.separator;
		LoadProperties lp = new LoadProperties();
		try {

			String parentDirName = lp.getProperty("LOGO_DIR");
			appIdFolder += parentDirName;
			logMgr.accessLog("parentDirName : " + parentDirName);
		} catch (Exception ex) {
			Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.SEVERE, null, ex);
		}
		return appIdFolder;

	}

	public boolean createAppIdFolders(String emailId) {
		LoadProperties lp = new LoadProperties();
		try {
			String[] emailUserName = emailId.split("@");
			logMgr.accessLog("AFTER SPLIT....:" + emailUserName[0]);
			String parentDirName = lp.getProperty("RECRUIT_FOLDER_DIR");
			logMgr.accessLog("parentDirName : " + parentDirName);

			String parentDirPath =  parentDirName + File.separator + emailUserName[0];
			logMgr.accessLog("parent path:" + parentDirPath);
			File parentDir = new File(parentDirPath);
			if (!parentDir.exists()) {
				parentDir.setExecutable(true, false);
				parentDir.setReadable(true, false);
				parentDir.setWritable(true, false);
				parentDir.mkdirs();
			}
			this.setDirpath(parentDirPath);

		} catch (Exception ex) {
			Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

	public boolean createAdminFolders() {
		LoadProperties lp = new LoadProperties();
		try {
			String parentDirName = lp.getProperty("ADMIN_FOLDER_DIR");
			logMgr.accessLog("parentDirName........ : " + parentDirName);
			String currYear = "";
			// String parentDirPath = parentDirName + File.separator + currYear;
			String parentDirPath = parentDirName;
			logMgr.accessLog("parent path:" + parentDirPath);
			File parentDir = new File(parentDirPath);
			if (!parentDir.exists()) {
				parentDir.setExecutable(true, false);
				parentDir.setReadable(true, false);
				parentDir.setWritable(true, false);
				parentDir.mkdirs();
			}
			this.setDirpath(parentDirPath);
		} catch (Exception ex) {
			Logger.getLogger(RecruitmentDirectoryOperation.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

}
