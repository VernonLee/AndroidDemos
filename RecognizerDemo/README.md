科大讯飞-语音听写Demo
----------------------------

####语音听写
目前语音听写支持__在线__和__离线__两种模式，离线模式需要下载语记(语音+)APP配合使用。

####配置过程
1. 官网注册应用，拿到APPID
2. 下载库和jar文件
3. 将libs下的文件拷贝到工程中
4. 如果使用Android Studio 1.3 以下版本，在.gradle文件中添加如下代码:

	```
	 sourceSets {
	        main {
	            jniLibs.srcDirs = ['libs']
	        }
	    }
	```
5. 在程序入口创建SpeechUtility

	```
	StringBuffer param = new StringBuffer();
	param.append(SpeechConstant.APPID + "=" + Constants.APP_ID);
	param.append(",");
	param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
	SpeechUtility.createUtility(this, param.toString());
	```
6. 编写测试代码，见demo中MainActivity.java


####demo使用
1. 下载sdk文件，名称格式为"平台_voice_appid", 添加libs中的.so库到工程中
2. 设置Constants.java中APPID。
