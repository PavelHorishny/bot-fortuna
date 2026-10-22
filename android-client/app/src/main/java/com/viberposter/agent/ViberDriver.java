final class ViberDriver implements MessengerDriver {
    private void dismissBanners() {
        clickIfPresent(ViberIds.BANNER_GOT_IT,null);
        clickIfPresent(ViberIds.BANNER_NOT_NOW, Ui.hasText(ViberIds.NOT_NOW));
        clickIfPresent(ViberIds.TYPING_TIP_CLOSE, null);

    }
}