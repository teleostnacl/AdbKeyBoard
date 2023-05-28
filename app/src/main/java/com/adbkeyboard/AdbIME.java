package com.adbkeyboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

@SuppressWarnings("FieldCanBeLocal")
public class AdbIME extends InputMethodService {
    /**
     * 传递需要输入文字的KEY
     */
    private final String KEY_MESSAGE = "msg";

    /**
     * 传递控制光标方向的KEY
     */
    private final String KEY_ORIENTATION = "orientation";

    /**
     * 传递控制的方向 上下左右 开头 末尾
     */
    private final String ORIENTATION_LEFT = "LEFT";
    private final String ORIENTATION_RIGHT = "RIGHT";
    private final String ORIENTATION_UP = "UP";
    private final String ORIENTATION_DOWN = "DOWN";
    private final String ORIENTATION_START = "START";
    private final String ORIENTATION_END = "END";

    // 输入文字
    private final String IME_INPUT_TEXT = "ADB_INPUT_TEXT";
    // 清除文字
    private final String IME_CLEAR_TEXT = "ADB_CLEAR_TEXT";
    // 回退文字
    private final String IME_BACKSPACE_TEXT = "ADB_BACKSPACE_TEXT";
    // 回车
    private final String IME_ENTER_ACTION = "ADB_ENTER_ACTION";
    // 控制光标
    private final String IME_CONTROL_CURSOR = "ADB_CONTROL_CURSOR";

    // 广播接收器
    private BroadcastReceiver mReceiver = null;

    @Override
    public View onCreateInputView() {
        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 注册广播接收器
        if (mReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IME_INPUT_TEXT);
            intentFilter.addAction(IME_CLEAR_TEXT);
            intentFilter.addAction(IME_BACKSPACE_TEXT);
            intentFilter.addAction(IME_ENTER_ACTION);
            intentFilter.addAction(IME_CONTROL_CURSOR);
            registerReceiver(mReceiver = new AdbReceiver(), intentFilter);
        }

        return textView;
    }

    public void onDestroy() {
        // 解注册广播接收器
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    /**
     * @return 横屏时EditText不全屏
     */
    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    /**
     * 配置接受广播事件的广播接收器
     */
    class AdbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            InputConnection ic = getCurrentInputConnection();
            if (ic == null) {
                return;
            }

            switch (intent.getAction()) {
                // 输入文字
                case IME_INPUT_TEXT: {
                    String msg = intent.getStringExtra(KEY_MESSAGE);
                    if (msg != null) {
                        ic.commitText(msg, 1);
                    }
                }
                break;

                // 清空
                case IME_CLEAR_TEXT: {
                    CharSequence curPos = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
                    CharSequence beforePos = ic.getTextBeforeCursor(curPos.length(), 0);
                    CharSequence afterPos = ic.getTextAfterCursor(curPos.length(), 0);
                    ic.deleteSurroundingText(beforePos.length(), afterPos.length());
                }
                break;

                // 回车
                case IME_ENTER_ACTION: {
                    inputKeyCode(ic, KeyEvent.KEYCODE_ENTER);
                }
                break;

                // 回退
                case IME_BACKSPACE_TEXT: {
                    inputKeyCode(ic, KeyEvent.KEYCODE_DEL);
                }
                break;

                // 控制光标
                case IME_CONTROL_CURSOR: {
                    switch (intent.getStringExtra(KEY_ORIENTATION)) {
                        case ORIENTATION_LEFT:
                            inputKeyCode(ic, KeyEvent.KEYCODE_DPAD_LEFT);
                            break;
                        case ORIENTATION_RIGHT:
                            inputKeyCode(ic, KeyEvent.KEYCODE_DPAD_RIGHT);
                            break;
                        case ORIENTATION_UP:
                            inputKeyCode(ic, KeyEvent.KEYCODE_DPAD_UP);
                            break;
                        case ORIENTATION_DOWN:
                            inputKeyCode(ic, KeyEvent.KEYCODE_DPAD_DOWN);
                            break;
                        case ORIENTATION_START:
                            inputKeyCode(ic, KeyEvent.KEYCODE_MOVE_HOME);
                            break;
                        case ORIENTATION_END:
                            inputKeyCode(ic, KeyEvent.KEYCODE_MOVE_END);
                            break;
                        default:
                    }
                }
                break;
                default:
            }
        }


        /**
         * 输入指定的键
         *
         * @param keyCode 指定的键
         */
        private void inputKeyCode(InputConnection ic, int keyCode) {
            KeyEvent ke = new KeyEvent(0, 0,
                    KeyEvent.ACTION_DOWN, keyCode,
                    0, 0, 0, 0,
                    KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE,
                    InputDevice.SOURCE_KEYBOARD);
            ic.sendKeyEvent(ke);
        }
    }
}
