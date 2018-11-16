package org.seuksa.frmk.tools.http.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileupLoadServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -269980652418909328L;
    private static final String TMP_DIR_PATH = "/temp";
    private File tmpDir;
    private static final String DESTINATION_DIR_PATH = "/files";
    private File destinationDir;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        try {
            final String tempPath = getServletContext().getRealPath(TMP_DIR_PATH);
            tmpDir = new File(tempPath);
            if (!tmpDir.isDirectory()) {
                throw new ServletException(TMP_DIR_PATH + " is not a directory");
            }
            final String realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH);
            destinationDir = new File(realPath);
            if (!destinationDir.isDirectory()) {
                throw new ServletException(DESTINATION_DIR_PATH + " is not a directory");
            }
        }
        catch (final Exception e) {
            log("Checking the directory", e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");
        final DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
        fileItemFactory.setRepository(tmpDir);
        final ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
        try {
            final List items = uploadHandler.parseRequest(request);
            final Iterator itr = items.iterator();
            while (itr.hasNext()) {
                final FileItem item = (FileItem) itr.next();
                if (!item.isFormField()) {
                    final File file = new File(destinationDir, item.getName());
                    item.write(file);
                }
            }
        }
        catch (final FileUploadException ex) {
            log("Error encountered while parsing the request", ex);
        }
        catch (final Exception ex) {
            log("Error encountered while uploading file", ex);
        }

    }

}
