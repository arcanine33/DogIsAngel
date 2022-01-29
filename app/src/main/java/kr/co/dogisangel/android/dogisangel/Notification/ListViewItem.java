package kr.co.dogisangel.android.dogisangel.Notification;


import android.graphics.drawable.Drawable;

public class ListViewItem {//알람의 아이템

    private int type ;

    private String titleStr ;
    private String talktext ;
    private String replytext ;
    private String ranktext ;


    private Drawable iconDrawable ;
    private Drawable iconDrawable2;


    private Drawable talkicon;
    private Drawable replyicon;
    private Drawable rankicon;





    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getTalktext() {
        return talktext;
    }

    public void setTalktext(String talktext) {
        this.talktext = talktext;
    }

    public String getReplytext() {
        return replytext;
    }

    public void setReplytext(String replytext) {
        this.replytext = replytext;
    }

    public String getRanktext() {
        return ranktext;
    }

    public void setRanktext(String ranktext) {
        this.ranktext = ranktext;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public Drawable getIconDrawable2() {
        return iconDrawable2;
    }

    public void setIconDrawable2(Drawable iconDrawable2) {
        this.iconDrawable2 = iconDrawable2;
    }

    public Drawable getTalkicon() {
        return talkicon;
    }

    public void setTalkicon(Drawable talkicon) {
        this.talkicon = talkicon;
    }

    public Drawable getReplyicon() {
        return replyicon;
    }

    public void setReplyicon(Drawable replyicon) {
        this.replyicon = replyicon;
    }

    public Drawable getRankicon() {
        return rankicon;
    }

    public void setRankicon(Drawable rankicon) {
        this.rankicon = rankicon;
    }
}
