###SLF4JDemo
-----------------------

####SLF4介绍

SLF4J(Simple Logging Facade for Java), 中文名称简单日志门面。它不是具体的日志系统，只是服务于日志系统。使用者选择期望的日志系统。通过SLF4J的API将其进行包装并提供统一的接口。

####配置

1. 选择目标日志系统，然后下载必要的jar包（这里采用的是LOG4J，使用android-logging-log4j.xx.xx.jar和log4j-xxx.jar）。* xx.xx代表版本号。
2. 下载log3j-xx.xx.jar,slf4j-log4j-xx.xx.jar
3. 创建应用的Application.java文件，开始日志属性配置：
	
	```
	private void configLogging() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        // 日志输出文件目录以及文件名称
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "test.log");
        // 是否在LogCat中显示信息
        logConfigurator.setUseLogCatAppender(true);
        // LogCat输出格式
        logConfigurator.setLogCatPattern("%m%n");
        // 是否写入文件中
        logConfigurator.setUseFileAppender(true);
        // 文件中日志显示格式
        logConfigurator.setFilePattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %C{1} - %m%n");
	    logConfigurator.setInternalDebugging(true);
        logConfigurator.setRootLevel(Level.INFO);
        logConfigurator.configure();
    }
	```
4. 验证是否配置成功。
	
	```
	public class MainActivity extends Activity {
	    private final Logger log = LoggerFactory.getLogger(MainActivity.class);

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	       ...

			generateLog();

		   ...
	    }

	    private void generateLog() {
	        log.info("在logcat和本地文件中找到此文件");
	    }
	}
	```
	如果配置成功，在之前配置的日志输出目录中找到test.log文件并打开，里面包含我们打印的日志。

####其他

1. 日志级别

* __DEGUG:__是最低限制日志等级，用于记录任何需要调试的信息，只能用于开发和测试环境
。在生产环境中绝对不能使用。
* __INFO:__高于DEBUG的限制，用于记录类似于服务器启动信息，接受到的发送的消息等等。
* __WARN:__高于INFO的限制，用于记录警告消息。比如，客户端和服务端之间断开连接。
* __ERROR:__高于WARN的限制， 用于记录报错和发生的异常。
* __FATAL:__用于记录发生非常严重的事件，这类事件可能导致应用程序发生中断。大多数情况下，出现这样的错误后应用将会崩溃并停止运行。
* __OFF:__在日志级别中最高，用于关闭日志系统。

2. 小技巧  

* 使用占位符 “{}” 提高效率  
  logger.debug("No of Orders " + noOfOrder + " for client : " + client); // slower 
  logger.debug("No of Executions {} for clients:{}", noOfOrder , client); // faster