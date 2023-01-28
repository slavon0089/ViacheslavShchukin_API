package org.example.utils;

public class Endpoints {

    private Endpoints() {
    }

    public static final String BASE_URI = "https://api.trello.com/1";
    public static final String BOARDS = "/boards";
    public static final String BOARDS_ID = "/boards/{id}";
    public static final String CARDS = "/cards";
    public static final String CARDS_ID = "/cards/{id}";
    public static final String LISTS = "/lists";
    public static final String CHECKLISTS = "/checklists";
    public static final String CHECKLISTS_ID = "/checklists/{id}";
}
