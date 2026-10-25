import java.util.function.Predicate;

final class ViberDriver implements MessengerDriver {
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