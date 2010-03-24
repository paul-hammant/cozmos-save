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

package com.thoughtworks.cozmos.sitemesh;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import com.opensymphony.module.sitemesh.mapper.DefaultDecorator;

public class EditModeDecoratorMapper extends AbstractDecoratorMapper {
    private static final String MAIN_DECORATOR = "/decorators/site.ftl";

    public Decorator getDecorator(HttpServletRequest request, Page page) {
        String pathToDecorator = MAIN_DECORATOR;

        if (isHackedComposer(request) | isMsWord(request)) {
            return null;
        }
        return new DefaultDecorator(pathToDecorator, pathToDecorator, null);
    }

    private boolean isMsWord(HttpServletRequest request) {
        String ua = request.getHeader("user-agent");
        return ua != null && ua.equals("Microsoft Data Access Internet Publishing Provider DAV");
    }

    private boolean isHackedComposer(HttpServletRequest request) {
        return "true".equalsIgnoreCase(request.getParameter("editMode"));
    }


}
