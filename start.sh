ADB_IME="com.adbkeyboard/.AdbIME"
DEFAULT_IME="com.baidu.input_mi/.ImeService"

### 使用
# 切换输入法
adb shell "ime enable $ADB_IME"
adb shell "ime set $ADB_IME"
# 启动输入的bash
konsole --hide-menubar --hide-tabbar -p TerminalColumns=35 -p TerminalRows=2 -p tabtitle="input" --noclose -e "sh ./input.sh"&
# 记录pid
pid=$! 


### 停止
# 杀死输入的bash
kill $pid

# 还原输入法
adb shell "ime set $DEFAULT_IME"
adb shell "ime disable $ADB_IME"