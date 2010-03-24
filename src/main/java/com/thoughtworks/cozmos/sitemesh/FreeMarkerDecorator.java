package com.thoughtworks.cozmos.sitemesh;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.Template;
import freemarker.ext.servlet.FreemarkerServlet;

public class FreeMarkerDecorator extends FreemarkerServlet {
    protected boolean preTemplateProcess(HttpServletRequest request, HttpServletResponse response,
            Template template, TemplateModel templateModel) throws ServletException, IOException {
        boolean result = super.preTemplateProcess(request, response, template, templateModel);

        SimpleHash hash = (SimpleHash) templateModel;

        HTMLPage htmlPage = (HTMLPage) request.getAttribute(RequestConstants.PAGE);

        String title, body, head;

        if (htmlPage == null) {
            title = "No Title";
            body = "No Body";
            head = "<!-- No head -->";
        } else {
            title = htmlPage.getTitle();

            StringWriter buffer = new StringWriter();
            htmlPage.writeBody(buffer);
            body = buffer.toString();

            buffer = new StringWriter();
            htmlPage.writeHead(buffer);
            head = buffer.toString();

            hash.put("page", htmlPage);
        }

        hash.put("title", title);
        hash.put("body", body);
        hash.put("head", head);
        hash.put("base", request.getContextPath());

        return result;
    }
}