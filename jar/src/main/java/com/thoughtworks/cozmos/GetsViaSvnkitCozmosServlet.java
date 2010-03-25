package com.thoughtworks.cozmos;

import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class GetsViaSvnkitCozmosServlet extends HttpServlet {

    private String svnUrl;
    private String newPageTemplate;
    private String defaultIndexFile;

    public void init(ServletConfig servletConfig) throws ServletException {

        svnUrl = servletConfig.getInitParameter("svn_url");
        newPageTemplate = servletConfig.getInitParameter("new_page_template_file");
        defaultIndexFile = servletConfig.getInitParameter("default_index_file");
        super.init(servletConfig);

        if (svnUrl.startsWith("http")) {
            DAVRepositoryFactory.setup();
        } else if (svnUrl.startsWith("svn")) {
            SVNRepositoryFactoryImpl.setup();
        } else if (svnUrl.startsWith("file:")) {
            FSRepositoryFactory.setup();
        } else {
            throw new ServletException("bad 'svn_url' init param, was expecting http(s), svn or file prefixed one");
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();

        if (path.endsWith("/")) {
            System.out.println("--> redirecting to " + path + defaultIndexFile);
            resp.sendRedirect(path + defaultIndexFile);
            return;
        }

        try {
            try {
                streamResource(path, resp);
            } catch (FourOhFour fourOhFour) {
                System.out.println("--> 404 " + svnUrl + " " + path);
                if (path.endsWith(".html")) {
                    streamResource(newPageTemplate, resp);
                } else {
                    resp.setStatus(404);
                }
            } catch (IsDirectory isDirectory) {
                resp.sendRedirect(path + defaultIndexFile);
            }
        } catch (RuntimeException e) {
            System.out.println("--> RE " + svnUrl + " " + path);
            e.printStackTrace();
        } catch (SVNException e) {
            e.printStackTrace();
        }

    }


    private void streamResource(String filePath, HttpServletResponse resp) throws SVNException, FourOhFour, IsDirectory, IOException {

        String name = "anonymous";
        String password = "anonymous";

        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        /*
         * Initializes the library (it must be done before ever using the
         * library itself)
         */

        SVNRepository repository = null;
        /*
        * Creates an instance of SVNRepository to work with the repository.
        * All user's requests to the repository are relative to the
        * repository location used to create this SVNRepository.
        * SVNURL is a wrapper for URL strings that refer to repository locations.
        */
        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));

        /*
         * User's authentication information (name/password) is provided via  an
         * ISVNAuthenticationManager  instance.  SVNWCUtil  creates  a   default
         * authentication manager given user's name and password.
         *
         * Default authentication manager first attempts to use provided user name
         * and password and then falls back to the credentials stored in the
         * default Subversion credentials storage that is located in Subversion
         * configuration area. If you'd like to use provided user name and password
         * only you may use BasicAuthenticationManager class instead of default
         * authentication manager:
         *
         *  authManager = new BasicAuthenticationsManager(userName, userPassword);
         *
         * You may also skip this point - anonymous access will be used.
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);

        /*
         * This Map will be used to get the file properties. Each Map key is a
         * property name and the value associated with the key is the property
         * value.
         */
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        /*
        * Checks up if the specified path really corresponds to a file. If
        * doesn't the program exits. SVNNodeKind is that one who says what is
        * located at a path in a revision. -1 means the latest revision.
        */
        SVNNodeKind nodeKind = repository.checkPath(filePath, -1);

        if (nodeKind == SVNNodeKind.NONE) {
            throw new FourOhFour(filePath);
        } else if (nodeKind == SVNNodeKind.DIR) {
            throw new IsDirectory();
        }

        OutputStream os = resp.getOutputStream();

        /*
        * Gets the contents and properties of the file located at filePath
        * in the repository at the latest revision (which is meant by a
        * negative revision number).
        */
        repository.getFile(filePath, -1, fileProperties, baos);

        /*
        * Here the SVNProperty class is used to get the value of the
        * svn:mime-type property (if any). SVNProperty is used to facilitate
        * the work with versioned properties.
        */
        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
        if (mimeType == null) {
            mimeType = getServletContext().getMimeType(filePath);
        }

        resp.setContentType(mimeType);

        /*
        * SVNProperty.isTextMimeType(..) method checks up the value of the mime-type
        * file property and says if the file is a text (true) or not (false).
        */
        boolean isTextType = SVNProperty.isTextMimeType(mimeType);

        Iterator iterator = fileProperties.nameSet().iterator();
        /*
         * Displays file properties.
         */
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = fileProperties.getStringValue(propertyName);
            System.out.println("File property: " + propertyName + "="
                    + propertyValue);
        }

        baos.writeTo(resp.getOutputStream());

        /*
         * Gets the latest revision number of the repository
         */
        long latestRevision = repository.getLatestRevision();
    }


}
