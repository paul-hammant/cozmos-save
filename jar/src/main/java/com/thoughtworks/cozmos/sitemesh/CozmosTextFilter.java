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

import com.opensymphony.module.sitemesh.html.TextFilter;
import com.thoughtworks.cozmos.ExtraneousWordDocConvertor;
import com.thoughtworks.cozmos.WikiWordsConvertor;

public class CozmosTextFilter implements TextFilter {
    public String filter(String text) {
        //System.err.println("1 -----------------------------------------");
        //System.err.println(text);
        //System.err.println("2 -----------------------------------------");
        text = new ExtraneousWordDocConvertor(text).convert();
        //System.err.println(text);
        //System.err.println("3 -----------------------------------------");
        text = new WikiWordsConvertor(text).convert();
        return text;
    }
}
