package com.viberposter.agent;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.function.Predicate;

final class ViberDriver implements MessengerDriver {
    
    private final AccessibilityService service;
    private final Ui ui;

    ViberDriver(AccessibilityService service){
        this.service = service;
        this.ui = new Ui(service);
    }

        @Override
    public void openChat(String chatName) throws AgentException {
        Intent i = service.getPackageManager().getLaunchIntentForPackage(ViberIds.PKG);
        if (i == null) {
            throw new AgentException("Viber не найден (проверь <queries> в манифесте)");
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        service.startActivity(i);

        // Если Viber открылся внутри какого-то чата, жмём «Назад», пока не появится главный экран
        for (int k = 0; k < 5 && ui.waitFor(ViberIds.MENU_SEARCH, null, 2000) == null; k++) {
            log("главный экран не виден, жму Назад");
            ui.back();
        }
        dismissBanners();

        // Сначала ищем чат прямо в списке, а если его там нет, через поиск
        AccessibilityNodeInfo row = ui.find(ViberIds.CHAT_ROW_NAME, Ui.hasText(chatName));
        if (row != null) {
            log("чат найден в списке");
            ui.click(row);
        } else {
            log("ищу чат через поиск");
            ui.click(require(ui.waitFor(ViberIds.MENU_SEARCH, null, 3000), "кнопка поиска"));
            AccessibilityNodeInfo field = require(ui.waitFor(ViberIds.SEARCH_TEXT, null, 3000), "поле поиска");
            ui.setText(field, chatName);
            ui.click(require(ui.waitFor(ViberIds.TITLE, Ui.hasText(chatName), 5000), "чат «" + chatName + "» в поиске"));
        }

        require(ui.waitFor(ViberIds.SEND_TEXT, null, 5000), "поле ввода в чате");
        require(ui.find(ViberIds.TITLE, Ui.hasText(chatName)), "заголовок чата «" + chatName + "»");
        dismissBanners();
        log("чат открыт: " + chatName);
    }

    private void dismissBanners() {
        clickIfPresent(ViberIds.BANNER_GOT_IT,null);
        clickIfPresent(ViberIds.BANNER_NOT_NOW, Ui.hasText(ViberIds.NOT_NOW));
        clickIfPresent(ViberIds.TYPING_TIP_CLOSE, null);
    }

    private void clickIfPresent(String id, Predicate <AccessibilityNodeInfo> match){
        AccessibilityNodeInfo n = ui.find(id,match);
        if(n!=null){
            log("closing banner "+ id);
            ui.click(n);//hello from spaces
            Ui.sleep(500);
        }
    }

    private static AccessibilityNodeInfo require(AccessibilityNodeInfo n, String what) throws AgentException {
        if (n == null){
            throw new AgentException("not found "+what);
        }
        return n;
    }

    private static void log(String msg){
        Log.i(Ui.TAG, msg);
        
    }
}