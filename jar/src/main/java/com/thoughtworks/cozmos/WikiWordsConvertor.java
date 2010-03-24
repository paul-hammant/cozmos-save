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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

public class WikiWordsConvertor {
    private static final String ANYTHING_BETWEEN_SQUARE_BRACES = "\\[(.*?)\\]";

    private static final Pattern WIKI_WORDS = Pattern.compile(ANYTHING_BETWEEN_SQUARE_BRACES, Pattern.DOTALL);

    private static final String SPACE = " ";

    private static final String UNDERSCORE = "_";

    private final String textFragment;

    public WikiWordsConvertor(String textFragment) {
        this.textFragment = textFragment;
    }

    public String convert() {
        Matcher wikiWordsMatcher = WIKI_WORDS.matcher(textFragment);
        StringBuffer buffer = new StringBuffer(textFragment);
        int nextStartingPoint = 0;
        while (wikiWordsMatcher.find()) {
            nextStartingPoint = processPotentialWikiWord(buffer, wikiWordsMatcher, nextStartingPoint);
        }
        return buffer.toString();
    }

    private int processPotentialWikiWord(StringBuffer buffer, Matcher wikiWordsMatcher, int fromThisPoint) {
        int startingPoint = buffer.indexOf(wikiWordsMatcher.group(), fromThisPoint);
        int endingPoint = endingPoint(startingPoint, wikiWordsMatcher);
        String wikiPhrase = wikiWordsMatcher.group(1);
       
        String result = "[" + wikiPhrase + "]";
        if (wikiPhrase.indexOf('[') == -1 && wikiPhrase.indexOf('<') == -1) {
            if (wikiPhrase.charAt(0) == '~') {
                buffer.replace(startingPoint, endingPoint, "[" + wikiPhrase.substring(1,wikiPhrase.length()) + "]");
            } else {
                StringTokenizer st = new StringTokenizer(wikiPhrase, "|");
                if (st.countTokens() == 1) {
                    result = toAnchor(wikiPhrase, wikiPhrase);
                } else if (st.countTokens() == 2) {
                    result = toAnchor(st.nextToken(), st.nextToken());
                }
                buffer.replace(startingPoint, endingPoint, result);
            }
        }
        return startingPoint + 2;
    }

    private static int endingPoint(int startingPoint, Matcher matcher) {
        return startingPoint + matcher.end() - matcher.start();
    }

    private static String toAnchor(String text, String linkWord) {
        return "<a href=\"/" + underscorify(linkWord) + ".html\">" + text.replaceAll("\n", SPACE) + "</a>";
    }

    private static String underscorify(String wikiWord) {
        return wikiWord.replaceAll(SPACE, UNDERSCORE).replaceAll("\n", UNDERSCORE);
    }
}
