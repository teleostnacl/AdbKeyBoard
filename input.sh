#!/bin/sh

STD_OUT="/dev/stdout"
STD_NULL="/dev/null"

# 重定向标注输出路径
stdout=$STD_NULL

# 定义常量 从代码中提取

# 控制传递字符串的KEY
KEY_MESSAGE="msg"

# 传递控制光标方向的KEY
KEY_ORIENTATION="orientation"

# 传递控制的方向 上下左右 开头 末尾
ORIENTATION_LEFT="LEFT"
ORIENTATION_RIGHT="RIGHT"
ORIENTATION_UP="UP"
ORIENTATION_DOWN="DOWN"
ORIENTATION_START="START"
ORIENTATION_END="END"

# 输入文字
IME_INPUT_TEXT="ADB_INPUT_TEXT"

# 清除文字
IME_CLEAR_TEXT="ADB_CLEAR_TEXT"

# 回退文字
IME_BACKSPACE_TEXT="ADB_BACKSPACE_TEXT"

# 回车
IME_ENTER_ACTION="ADB_ENTER_ACTION"

# 控制光标
IME_CONTROL_CURSOR="ADB_CONTROL_CURSOR"


while read -p "> " -e input; do

	case "$input" in
		# 空白字符 回车
		"" ) adb shell "am broadcast -a $IME_ENTER_ACTION" >$stdout
			;;

		# \c clear 清空
		"\c" ) adb shell "am broadcast -a $IME_CLEAR_TEXT" >$stdout
			;;

		# \b backspace 回退
		"\b" ) adb shell "am broadcast -a $IME_BACKSPACE_TEXT" >$stdout
			;;

		# \l 控制光标左移
		"\l" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_LEFT" >$stdout
			;;

		# \r 控制光标右移
		"\r" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_RIGHT" >$stdout
			;;

		# \u 控制光标上移
		"\u" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_UP" >$stdout
			;;

		# \d 控制光标下移
		"\d" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_DOWN" >$stdout
			;;

		# \s 控制光标开头
		"\s" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_START" >$stdout
			;;

		# \e 控制光标结尾
		"\e" ) adb shell "am broadcast -a $IME_CONTROL_CURSOR --es $KEY_ORIENTATION $ORIENTATION_END" >$stdout
			;;

		# 输入的字符串
		* ) adb shell "am broadcast -a $IME_INPUT_TEXT --es $KEY_MESSAGE \"$input\"" >$stdout
			;;
	esac

done