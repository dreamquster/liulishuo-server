###liulishuo-server
####目录
/AutoTeller 是webapp的工程目录，服务监听http://127.0.0.1:8080
/OpsMonitor 是运营监控工程,服务监听http://127.0.0.1:8081

用mysql -uroot登录vagrant中的MySQL
数据库用use coins

####打包
```shell
#会在bulild2tar.sh所在目录生成一个liulishuo.tgz
sh ./bulild2tar.sh
```

####测试
```shell
cd AutoTeller
#测试覆盖率
mvn cobertura:cobertura
#然后到AutoTeller/target/site/cobertura/index.html查看总体报告

#单元测试，上一步时已显示
mvn test
```

####使用
服务使用脚本：
```shell
sh autoteller.sh start|stop|restart|status
```

