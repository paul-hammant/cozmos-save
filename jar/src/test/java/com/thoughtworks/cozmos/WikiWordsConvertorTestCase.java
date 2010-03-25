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

import junit.framework.TestCase;

public class WikiWordsConvertorTestCase extends TestCase {

    public void testShouldNotConvertEmptyText() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("");
        assertEquals("", convertor.convert());
    }

    public void testShouldNotConvertPlainText() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("The quick brown fox");
        assertEquals("The quick brown fox", convertor.convert());
    }

    public void testShouldNotConvertHTMLAnchors()  {
        WikiWordsConvertor convertor = new WikiWordsConvertor("<a href=\"/not_a_wiki_link.html\">not a wiki link</a>");
        assertEquals(anchor("not_a_wiki_link", "not a wiki link"), convertor.convert());
    }

    public void testShouldConvertSingleWordWikiWordLinkToSimpleHTMLAnchor() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[simple]");
        assertEquals(anchor("simple"), convertor.convert());
    }

    public void testShouldConvertMultipleWordWikiWordLinksToHTMLAnchorsWithUnderscores()  {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[more complicated]");
        assertEquals(anchor("more_complicated", "more complicated"), convertor.convert());
    }

    public void testShouldConvertMultipleWikiWordLinksToSeparateHTMLAnchors()  {
        WikiWordsConvertor simpleConvertor = new WikiWordsConvertor("[foo][bar]");
        assertEquals(anchor("foo") + anchor("bar"), simpleConvertor.convert());

        WikiWordsConvertor multiWordConvertor = new WikiWordsConvertor("[foo bar][goo gle]");
        assertEquals(anchor("foo_bar", "foo bar") + anchor("goo_gle", "goo gle"), multiWordConvertor.convert());
    }

    // Sometimes Composer splits [an apparent link] over more than one line.
    public void testShouldConvertMultipleLineWikiWordToAnchors()  {
        WikiWordsConvertor simpleConvertor = new WikiWordsConvertor("[foo\nbar]");
        assertEquals(anchor("foo_bar", "foo bar"), simpleConvertor.convert());
    }


    public void testShouldNotConvertMismatchedLeftSquareBraces()  {
        WikiWordsConvertor emptyLeftSquareBraceConvertor = new WikiWordsConvertor("[[broken]");
        assertEquals("[[broken]", emptyLeftSquareBraceConvertor.convert());

        WikiWordsConvertor errantLeftSquareBraceConvertor = new WikiWordsConvertor("[word[broken]");
        assertEquals("[word[broken]", errantLeftSquareBraceConvertor.convert());
    }

    public void testShouldLeaveExtraRightSquareBracesAlone() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[broken]]");
        assertEquals(anchor("broken") + "]", convertor.convert());
    }

    public void testShouldNotConvertWikiWordsWhichContainHTML()  {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[with html <br>]");
        assertEquals("[with html <br>]", convertor.convert());

        WikiWordsConvertor errantLeftAngleBracketConvertor = new WikiWordsConvertor("[with html <]");
        assertEquals("[with html <]", errantLeftAngleBracketConvertor.convert());
    }

    public void testTroublesomeCase1() {
        String foo = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r" +
                "<head>\r" +
                "  <title></title>\r" +
                "</head>\r" +
                "<body>\r" +
                "hello<br>\r" +
                "<br>\r" +
                "d<img style=\"width: 196px; height: 273px;\" alt=\"foo\" src=\"wheaties.jpg\"><br>\r" +
                "</body>\r" +
                "</html>";
        WikiWordsConvertor fullHTMLTagConvertor = new WikiWordsConvertor(foo);
        assertEquals(foo, fullHTMLTagConvertor.convert());

    }

    public void testShouldHandleAlias() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[leg of toad|witchcraft happens]");
        assertEquals(anchor("witchcraft_happens","leg of toad"), convertor.convert());
    }
    public void testShouldNotHandleTwoBarsAsAlias() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[leg of |toad|witchcraft happens]");
        assertEquals("[leg of |toad|witchcraft happens]", convertor.convert());
    }


    public void testTickAfterLeftSquareBracketEscapesItAll() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[~this one should not wikify but should omit the tilde]");
        assertEquals("[this one should not wikify but should omit the tilde]", convertor.convert());
    }

    public void testMultipleWikiLinks() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[one] two [three] four [five]");
        assertEquals("<a href=\"/one.html\">one</a> two <a href=\"/three.html\">three</a> four <a href=\"/five.html\">five</a>", convertor.convert());
    }

    public void testMultipleWikiLinksEachWithLongerAlias() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[1|one] two [3|three] four [5|five]");
        assertEquals("<a href=\"/one.html\">1</a> two <a href=\"/three.html\">3</a> four <a href=\"/five.html\">5</a>", convertor.convert());
    }

    public void testMultipleWikiLinksEachWithShorterAlias() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("[one|1] two [three|3] four [five|5]");
        assertEquals("<a href=\"/1.html\">one</a> two <a href=\"/3.html\">three</a> four <a href=\"/5.html\">five</a>", convertor.convert());
    }

    public void testToubleSomeCase() {
        WikiWordsConvertor convertor = new WikiWordsConvertor("x.\n" +
                "Thus [~hello how are you|greeting] becomes [hello how are you|greeting].");
        assertEquals("x.\n" +
                "Thus [hello how are you|greeting] becomes <a href=\"/greeting.html\">hello how are you</a>.", convertor.convert());
    }

    // public void testShouldIgnoreLeftSquareBracketsInsideHTMLPreTags() throws
    // Exception {
    // WikiWordsConvertor convertor = convertor("<pre>[</pre>");
    // assertEquals("<pre>[</pre>", convertor.convert());
    //        
    // convertor = convertor("<pre>[unconverted]</pre>");
    // assertEquals("<pre>[unconverted]</pre>", convertor.convert());
    // }

    private static String anchor(String link) {
        return anchor(link, link);
    }

    private static String anchor(String href, String label) {
        return "<a href=\"/" + href + ".html\">" + label + "</a>";
    }

}
