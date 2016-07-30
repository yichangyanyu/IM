package com.lishuang.administrator.im;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lishuang.administrator.im.adapter.ExpressionAdapter;
import com.lishuang.administrator.im.adapter.MessageAdapter;
import com.lishuang.administrator.im.model.ChatMessage;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button mButtonLeft;//左边发送按钮
    private Button mButtonRight;//右边发送按钮
    private EditText mEditTextInput;//输入框
    private ListView mListView;//显示消息的ListView
    private GridView mGridView;//显示表情的GridView
//    private PopupWindow mPopupWindow;
//    private LinearLayout mLinearLayout;
//    private LayoutInflater mInflater;

    private ImageButton mImageButtonExpression;//弹出和收回表情框的按钮
    private Spanned mSpanned;//富文本
    private Html.ImageGetter mImageGetter;//获得富文本图片
    private List<ChatMessage> mData;//消息数据
    private MessageAdapter mMessageAdapter;//消息适配器
    private ExpressionAdapter mExpressionAdapter;//表情适配器
    private InputMethodManager mInputMethodManager;//用于控制手机键盘的显示有否的对象（此处）
    //表情数据名称
    private String[] mExpression = {"dra", "drb", "drc", "drd", "dre", "drf",
            "drg", "drh", "dri", "drg", "drk", "drl",
            "drm", "drn", "dro", "drp", "drq", "drr",
            "drs", "drt", "dru", "drv", "drw", "drx",
            "dry", "drz", "dra"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mInflater = getLayoutInflater();
        mButtonLeft = (Button) findViewById(R.id.button_left);
        mButtonRight = (Button) findViewById(R.id.button_right);
        mEditTextInput = (EditText) findViewById(R.id.edittext_input);
        mImageButtonExpression = (ImageButton) findViewById(R.id.imagebutton_expression);
        mListView = (ListView) findViewById(R.id.listview);
//        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
//        mPopupWindow = new PopupWindow(MainActivity.this);
//        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        View popupView = mInflater.inflate(R.layout.popupwindow, null);
//        mPopupWindow.setContentView(popupView);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setOutsideTouchable(true);
//        mGridView = (GridView) popupView.findViewById(R.id.gridview);

        mGridView = (GridView) findViewById(R.id.gridview);
        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);
        mImageButtonExpression.setOnClickListener(this);
        mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //消息数据初始化
        mData = new ArrayList<ChatMessage>();
        //通过反射获得图片的id
        mImageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                Drawable drawable = null;
                int id = R.mipmap.dra;
                if (s != null) {
                    Class clazz = R.mipmap.class;
                    try {
                        Field field = clazz.getDeclaredField(s);
                        id = field.getInt(s);

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
                drawable = getResources().getDrawable(id);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
        mExpressionAdapter = new ExpressionAdapter(getLayoutInflater());
        mGridView.setAdapter(mExpressionAdapter);
        //点击表情，将表情添加到输入框中。
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //通过mImageGetter获得id获得表情图片，然后将其添加到输入框中。
                mSpanned = Html.fromHtml("<img src='" + mExpression[position] + "'/>", mImageGetter, null);
                mEditTextInput.getText().insert(mEditTextInput.getSelectionStart(), mSpanned);

            }
        });
        //点击输入框收回表情框
        mEditTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGridView.getVisibility() == View.VISIBLE) {
                    mGridView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        mMessageAdapter = new MessageAdapter(getLayoutInflater(), mData, mImageGetter);
        switch (view.getId()) {
            case R.id.imagebutton_expression://添加表情的按钮

                if (mGridView.getVisibility() == View.VISIBLE) {
                    mGridView.setVisibility(View.GONE);
                } else {
                    mGridView.setVisibility(View.VISIBLE);
                    mInputMethodManager.hideSoftInputFromWindow(mEditTextInput.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }
//                Log.d("data", "点击了表情按钮 ");
//                mPopupWindow.showAtLocation(mLinearLayout, 0, 0 ,0);
                break;
            case R.id.button_left://左边的发送按钮
                showListViewLeft();
                break;
            case R.id.button_right://右边的发送按钮
                showListViewRight();
                break;
            default:
                break;

        }

    }
    /*
    发送左边消息
     */
    private void showListViewRight() {
        ChatMessage dataRight = new ChatMessage();
        dataRight.setTextViewTime(System.currentTimeMillis());
        dataRight.setTextViewHonour("营长");
        dataRight.setTextviewName("虫虫");
        /*
        判断发送的消息是否为空，如果为空则弹出提示不允许发送
        */
        if (filterHtml(Html.toHtml(mEditTextInput.getText())).equals("")) {
            Toast.makeText(getApplicationContext(), "发送的消息不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        dataRight.setTextViewInput(filterHtml(Html.toHtml(mEditTextInput.getText())));
        dataRight.setType(MessageAdapter.SEND_RIGHT);
        mMessageAdapter.notifyDataSetChanged();
        mData.add(dataRight);
        mListView.setAdapter(mMessageAdapter);
        mListView.setSelection(mData.size() - 1);
        mEditTextInput.setText("");
    }
    /*
    发送右边消息
     */
    private void showListViewLeft() {
        ChatMessage dataLeft = new ChatMessage();
        dataLeft.setTextViewTime(System.currentTimeMillis());
        dataLeft.setTextViewHonour("营长");
        dataLeft.setTextviewName("虫虫");
        /*
        判断发送的消息是否为空，如果为空则弹出提示不允许发送
        */
        if (filterHtml(Html.toHtml(mEditTextInput.getText())).equals("")) {
            Toast.makeText(getApplicationContext(), "发送的消息不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //将解析的数据添加到输入框中。
        dataLeft.setTextViewInput(filterHtml(Html.toHtml(mEditTextInput.getText())));
        dataLeft.setType(MessageAdapter.SEND_LEFT);
        mMessageAdapter.notifyDataSetChanged();
        mData.add(dataLeft);
        mListView.setAdapter(mMessageAdapter);
        mListView.setSelection(mData.size() - 1);
        mEditTextInput.setText("");
    }
    public String filterHtml(String str) {
        str = str.replaceAll("<(?!br|img)[^>]+>", "").trim();
        return str;
    }
}
