package com.viberposter.agent;

final class ViberIds {
    static final String PKG = "com.viber.voip";

    private static String id (String name) {
        return PKG + ":id/" + name;
    }

    static final String MENU_SEARCH = id("menu_search");
    static final String CHAT_ROW_NAME = id("from");

    static final String SEARCH_TEXT = id("search_src_text");

    static final String TITLE = id("title");

    static final String SEND_TEXT = id("send_text");
    static final String BTN_SEND = id("btn_send");
    static final String MESSAGE_TEXT = id("textMessageView");

    static final String MENU = id("menu");

    static final String DIALOG_BUTTON_1 = id("button1");

    static final String BANNER_GOT_IT = id("gotIt");
    static final String BANNER_NOT_NOW = id("secondary_button");
    static final String TYPING_TIP_CLOSE = id("hide_is_typing_ftue_close_click_area");

    static final String DESC_SEND = "Отправить";
    static final String DESC_EDIT = "Изменить";
    static final String MENU_EDIT = "Изменить";
    static final String MENU_DELETE = "Удалить";
    static final String DELETE_EVERYWHERE = "Удалить везде";
    static final String NOT_NOW = "Не сейчас";

    private ViberIds() {}
}