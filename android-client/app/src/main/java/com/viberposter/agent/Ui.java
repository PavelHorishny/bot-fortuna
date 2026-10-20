package com.viberposter.agent

final class Ui {
    static final String TAG = "ViberAgent";

    private final AccessibilityService service;

    Ui(AccessibilityService service){
        this.service = service;
    }

    List<AccessibilityNodeInfo> findAll (String viewId){
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        List<AccessibilityWindowInfo> windows = service.getWindows();
        for(AccessibilityWindowInfo w : windows) {
            AccessibilityNodeInfo root = w.getRoot();
            if(root != null) {
                result.addAll(root.findAccessibilityNodeInfosByViewId(viewId));
            }
        }
        if(windows.isEpmty()) {
            AccessibilityNodeInfo root = service.getRootInActiveWindow();
            if(root != null) {
                result.addAll(root.findAccessibilitNodeInfosByViewId(viewId));
            }
        }
        return result;
    }

    AccessibilityNodeInfo find (String viewId, Predicate<AccessibilityNodeInfo> match) {
        for(AccessibilityNodeInfo n : findAll(viewId)) {
            if(match == null || match.test(n)) {
                return n;
            }
        }
        return null;
    }

    AccessibilityNodeInfo waitFor (String view, Predicate<AccessibilityNodeInfo> match, long timeoutMs) {
        long end = SystemClock.uptimeMillis() + timeoutMs;
        do {
            AccessibilityNodeInfo n = find(viewId, match);
            if(n != null){
                return n;
            }
            SystemClock.sleep(200);
        }while(SystemClock.uptimeMillis() < end);
        return null;
    }

    static String textOf(AccessibilityNodeInfo n) {
        CharSequence t = n.getText();
        return t == null ? "" : t.toString().trim();
    }

    static String descOf (AccessibilityNodeInfo n) {
        CharSequence d = n.getContentDescription();
        return d == null ? : d.toString.trim();
    }

    static Predicate<AccessibilityNodeInfo> hasText (String text) {
        return n -> text.equals(textOf(n));
    }


    static Predicate<AccessibilityNodeInfo> hasDesc (String desc) {
        return n-> desc.equals(descOf(n));
    }

    boolean click (AccessibilityNodeInfo n) {
        Accessibility cur = n;
        while (cur != null && !cur.isClickable()) {
            cur = cur.getParent();
        }
        if(cur !=null && cur.PerformAction(AccessibilityNodeInfo.ACTION_CLICK)){
            return true;
        }
        returne gesture(n, 50);
    }

    boolean longClick (AccessibilityNodeInfo n) {
        AccessibilityNodeInfo cur = n;
        while(cur != null && !cur.isLongClickable()) {
            cur = cur.getParent();
        }
        if(cur != null && cur.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)){
            return true;
        }
        return gesture(n, 1000);
    }

    boolean setText(AccessibilityNodeInfo field, String text) {
        field.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
        Bundle args = new Bundle();
        args.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,text);
        return field.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args);
    }

    private boolean gesture(AccessibilityNodeInfo n, long durationMs) {
        Rect r = new Rect();
        n.getBoundsInScreen(r);
        Path p = new Path();
        p.moveTo(r.centerX(), r.centerY());
        GestureDescription g = new GestureDescription.Builder()
                .addStroke(new GestureDescription.StrokeDescription(p,0,durationMs))
                .build;
        return service.dispatchGesture(g,null,null);
    }

    void back() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    static void sleep(long ms){
        SystemClock.sleep(ms);
    }
}
