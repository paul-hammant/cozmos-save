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

import junit.framework.TestCase;

import com.opensymphony.module.sitemesh.Page;

public class WikiWordsParserTestCase extends TestCase {

    private WikiWordsParser parser;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new WikiWordsParser();
    }

    public void xtestShouldConvertWikiWordsOutsideOfAnyHTMLTagsIntoHTMLAnchors() throws Exception {
        Page page = parser.parse("[hello world]".toCharArray());
        assertEquals("<a href=\"hello_world\">hello world</a>", page.getPage());
    }

    public void testShouldNotConvertWikiWordsInsideHTMLPreTagsToAnchors() throws Exception {
        Page page = parser.parse("<pre>[should not be converted]</pre>".toCharArray());
        assertEquals("<pre>[should not be converted]</pre>", page.getPage());
    }
}
