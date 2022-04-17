package com.example.quizgame;

public class Gift {
    private String giftImage, giftName, link;
    private int giftPrice;

    public Gift() {

    }

    public Gift(String giftImage, String giftName, int giftPrice) {
        this.giftImage = giftImage;
        this.giftName = giftName;
        this.giftPrice = giftPrice;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(int giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "giftImage='" + giftImage + '\'' +
                ", giftName='" + giftName + '\'' +
                ", link='" + link + '\'' +
                ", giftPrice=" + giftPrice +
                '}';
    }
}
