package ehiring.action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ehiring.dao.LogManager;
import ehiring.operation.PostOperation;
import ehiring.operation.RecruitmentDirectoryOperation;

@WebServlet("/UploadFileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
        maxFileSize = 1024 * 1024 * 50, // 50 MB
        maxRequestSize = 1024 * 1024 * 100) // 100 MB
public class UploadFileServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    LogManager logMgr;

    /**
     * Directory where uploaded files will be saved, its relative to the web
     * application directory.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Called UploadFileServlet do post");
        String emailLogId = "user_upload";
        logMgr = LogManager.getInstance(emailLogId);
        logMgr.accessLog("Called UploadFileServlet do post");

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        String returnMsg = "Request received. Not processed yet";
        if (isMultipart) {
            logMgr.accessLog("In multipart");
            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            String applicationPath = "";// request.getServletContext().getRealPath("");
            // constructs path of the directory to save uploaded file
            // String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR +
            // File.separator;

            try {
                String UPLOAD_DIR = "", advtNo = "", postNo = "", user = "", filetype = "";
                // Parse the request
                List items = upload.parseRequest(request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String value = item.getString();
                        //value = value.replace("/eRecruitment_NRSC", "");
                        logMgr.accessLog("1 : name : " + name + ", value : " + value + ", applicationPath is:" + applicationPath);
                        System.out.println("1 : name : " + name + ", value : " + value + ", applicationPath is:" + applicationPath);

                        if (name.equalsIgnoreCase("dir")) {
                            UPLOAD_DIR = applicationPath + File.separator + value;
                        } else if (name.equalsIgnoreCase("adv")) {
                            advtNo = value;
                        } else if (name.equalsIgnoreCase("post")) {
                            postNo = value;
                        } else if (name.equalsIgnoreCase("email")) {
                            user = value;
                            logMgr = LogManager.getInstance(user);
                        } else if (name.equalsIgnoreCase("filetype")) {
                            filetype = value;
                        }
                    }
                }

                boolean adminFlag = false;
                String userType = new PostOperation(emailLogId).getUserType(user).toLowerCase();
                if (userType != null) {
                    if (userType.equalsIgnoreCase("administrator")) {
                        adminFlag = true;
                    }
                }
                logMgr.accessLog("UPLOAD SEVLET....:" + user + ",adminFlag:" + adminFlag + "UPLOAD_DIR:" + UPLOAD_DIR + ",advtNo is:" + advtNo);

                String uploadFilePath = UPLOAD_DIR + File.separator + advtNo + File.separator + postNo + File.separator;
                if (filetype.equalsIgnoreCase("advertisement")) {
                    uploadFilePath = UPLOAD_DIR;

                }

                // creates the save directory if it does not exists
                File fileSaveDir = new File(uploadFilePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                logMgr.accessLog("fileSaveDir : " + fileSaveDir);

                iterator = items.iterator();
                while (iterator.hasNext()) {
                    //logMgr.accessLog("fileSaveDir 11111: " + fileSaveDir);
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                        logMgr.accessLog("fileSaveDir 222: " + fileSaveDir);
                        String fileName = item.getName();
                        // String root = getServletContext().getRealPath("/");
                        // File path = new File(root + "/uploads");
                        boolean isDirAv = fileSaveDir.exists();
                        // if (!isDirAv) {
                        // isDirAv = path.mkdirs();
                        // }
                        logMgr.accessLog("fileSaveDir333 : " + isDirAv);
                        if (isDirAv) {
                            File uploadedFile = new File(fileSaveDir + "/" + fileName);
                            returnMsg = "Success: File uploaded";
                            String extn = getFileExtension(uploadedFile);
                            logMgr.accessLog("uploadedFile is :" + uploadedFile + ", extension is:" + extn);
                            item.write(uploadedFile);
                            if (!adminFlag) {
                                File oldfile = uploadedFile;
                                String newFileStr = fileSaveDir + "/" + filetype + "_" + user.replace(".", "_").replaceFirst("@", "_") + extn;
                                File newfile = new File(newFileStr);
                                if (newfile.exists()) {
                                    newfile.delete();
                                }
                                if (!oldfile.exists()) {
                                    returnMsg = "Error: File does not exist. Please upload again.";

                                }
                                else { // ChangeId: 2023121302
                                    Files.copy(oldfile.toPath(), newfile.toPath());
                                }
                                
                                // Start: ChangeId: 2023121302
                                /* 
                                if (oldfile.renameTo(newfile)) {
                                    logMgr.accessLog("Rename succesful");
                                } else {
                                    logMgr.accessLog("Rename failed");
                                }
                                Files.copy(newfile.toPath(), oldfile.toPath());
                                */
                                
                                // End: ChangeId: 2023121302
                            }
                            // logMgr.accessLog("fileSaveDir6666 : " + uploadedFile);							
                            // logMgr.accessLog("fileSaveDir7777 : " + uploadedFile);
                        } else {
                            returnMsg = "Error: Directory path for file not available";
                        }
                    }
                }

            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        logMgr.accessLog("contentDisp......" + contentDisp);
        logMgr.accessLog("content-disposition header= " + contentDisp);
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        String exn = name.substring(lastIndexOf);
        return exn;
    }

    /*
     * public static void main(String args[]) { String file =
     * "F:\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\eRecruitment_NRSC\\Recruitment_data\\applicant\\saikalpana_t_nrsc_gov_in\\abcd1234\\121\\erec_main.jpg";
     * UploadFileServlet upf = new UploadFileServlet(); String extn =
     * upf.getFileExtension(new File(file)); logMgr.accessLog("extn:"+extn);
     * 
     * }
     */
}
