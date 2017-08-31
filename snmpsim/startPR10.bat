set JAVA_HOME=C:\Program Files\Java\jdk1.6.0

rem set clspath=%JAVA_HOME%\lib\dt.jar;
rem set clspath=%clspath%\;%JAVA_HOME%\lib\tools.jar;
set clspath=%clspath%\;.lib\commons-io-1.3.2.jar;.\lib\commons-net-3.3.jar;.\lib\snmp4j-1.10.1.jar;.\lib\snmp4j-agent-1.3.1.jar;.\lib\spring-core-4.2.3.RELEASE.jar;.\snmpsim-1.0.jar;

set path=%JAVA_HOME%\bin;%JAVA_HOME%;%path%

rem JPDA options. Uncomment and modify as appropriate to enable remote debugging.
set JAVA_OPTS=%JAVA_OPTS% -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8785,server=y,suspend=n

java -cp %clspath% %JAVA_OPTS% com.mw.omc.tool.snmp.simulator.PR10Simulator
pause