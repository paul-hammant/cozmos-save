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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ExtraneousWordDocConvertor {

    private static final Pattern SPELLING_ISSUES = Pattern.compile("<span class=SpellE>(.*?)</span>", Pattern.DOTALL);
    private static final Pattern GRAMMAR_ISSUES = Pattern.compile("<span class=GramE>(.*?)</span>", Pattern.DOTALL);

    private String textFragment;

    public ExtraneousWordDocConvertor(String textFragment) {
        this.textFragment = textFragment;
    }

    public String convert() {
        Matcher matcher = SPELLING_ISSUES.matcher(textFragment);
        StringBuffer buffer = new StringBuffer(textFragment);
        while (matcher.find()) {
            int startingPoint = buffer.indexOf(matcher.group());
            int endingPoint = endingPoint(startingPoint, matcher);
            String phrase = matcher.group(1);
            buffer.replace(startingPoint, endingPoint, phrase);
        }
        matcher = GRAMMAR_ISSUES.matcher(buffer);
        while (matcher.find()) {
            int startingPoint = buffer.indexOf(matcher.group());
            int endingPoint = endingPoint(startingPoint, matcher);
            String phrase = matcher.group(1);
            buffer.replace(startingPoint, endingPoint, phrase);
        }

        return buffer.toString();
    }

    private static int endingPoint(int startingPoint, Matcher matcher) {
        return startingPoint + matcher.end() - matcher.start();
    }
}
