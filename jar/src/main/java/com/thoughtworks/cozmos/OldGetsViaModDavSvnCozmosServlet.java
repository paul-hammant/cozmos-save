/*
 * Copyright (c) 2006, 2007 ThoughtWorks, Inc.
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */
package com.thoughtworks.cozmos;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OldGetsViaModDavSvnCozmosServlet extends HttpServlet {

    private String targetURL;
    private String newPageTemplate;
    private String defaultIndexFile;

    public void init(ServletConfig servletConfig) throws ServletException {

        targetURL = servletConfig.getInitParameter("mod_dav_svn_url");
        newPageTemplate = servletConfig.getInitParameter("new_page_template_file");
        defaultIndexFile = servletConfig.getInitParameter("default_index_file");
        super.init(servletConfig);


    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String path = req.getServletPath();

        System.err.println("--> req MDSPS " + path);

        if (path.endsWith("/")) {
            resp.sendRedirect(path + defaultIndexFile);
            return;
        }
        URL url = new URL(targetURL + path);

        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();

        int rc = urlc.getResponseCode();

        if (rc == HttpURLConnection.HTTP_NOT_FOUND) {
            url = new URL(targetURL + newPageTemplate);
            urlc = (HttpURLConnection) url.openConnection();
            rc = urlc.getResponseCode();
        }

        if (rc == HttpURLConnection.HTTP_OK) {
            String mimeType = getServletContext().getMimeType(path);
            resp.setContentType(mimeType);
            InputStream is = urlc.getInputStream();
            OutputStream os = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) >= 0) {
                os.write(buffer, 0, length);
            }
        } else {
            resp.sendError(rc);
        }

    }

}
