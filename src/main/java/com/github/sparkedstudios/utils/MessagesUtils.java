package com.github.sparkedstudios.utils;

public class MessagesUtils {

    // -------------- Version 1.0 ------------------

    public static final Message NOPERMISSION = new Message("no-permission");
    public static final Message SUBCOMMANDNOTFOUND = new Message("sub-command-not-found");
    public static final Message USAGE = new Message("usage");
    public static final Message NO_CONSOLE = new Message("no-console");
    public static final Message HEAD_EXISTS = new Message("head-exists");
    public static final Message HEAD_CREATED = new Message("head-created");
    public static final Message HEAD_DELETED = new Message("head-deleted");
    public static final Message HEAD_MOVED = new Message("head-moved");
    public static final Message HEAD_NOT_FOUND = new Message("head-not-found");
    public static final Message RELOADED = new Message("reloaded");
    public static final Message GIVE = new Message("give-head");

    public static Message of(String path) {
        return new Message(path);
    }
}