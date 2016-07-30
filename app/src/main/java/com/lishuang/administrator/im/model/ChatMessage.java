package com.lishuang.administrator.im.model;

import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/8/31.
 */
public class ChatMessage {

    private int imageViewPerson;//人物头像
    private long textViewTime;//显示的时间
    private String textViewHonour;//人物头衔
    private String textviewName;//人物昵称
    private String textViewInput;//说话内容
    private int type;//信息类型，是在左边显示还是右边显示。

    /*
    定义两个构造器，一个无参，一个传值。
     */
    public ChatMessage() {
    }
    public ChatMessage(int imageViewPerson, long textViewTime, String textViewHonour, String textviewName, String textViewInput) {
        this.imageViewPerson = imageViewPerson;
        this.textViewTime = textViewTime;
        this.textViewHonour = textViewHonour;
        this.textviewName = textviewName;
        this.textViewInput = textViewInput;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImageViewPerson() {
        return imageViewPerson;
    }

    public void setImageViewPerson(int imageViewPerson) {
        this.imageViewPerson = imageViewPerson;
    }

    public long getTextViewTime() {
        return textViewTime;
    }

    public void setTextViewTime(long textViewTime) {
        this.textViewTime = textViewTime;
    }

    public String getTextViewHonour() {
        return textViewHonour;
    }

    public void setTextViewHonour(String textViewHonour) {
        this.textViewHonour = textViewHonour;
    }

    public String getTextviewName() {
        return textviewName;
    }

    public void setTextviewName(String textviewName) {
        this.textviewName = textviewName;
    }

    public String getTextViewInput() {
        return textViewInput;
    }

    public void setTextViewInput(String textViewInput) {
        this.textViewInput = textViewInput;
    }
}
