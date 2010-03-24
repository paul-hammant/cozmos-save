package com.thoughtworks.cozmos;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNErrorMessage;

public class FourOhFour extends RuntimeException {
    public FourOhFour(String message) {
        super(message);
    }
}
