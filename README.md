# goodProgramming
积累工作中使用的一些常用技巧  

###linux一些常用的命令:
* ssh 连接服务器
* scp 源文件  目的机器地址  
eg:scp web-1.0-SNAPSHOT.jar root@192.168.1.10:/app/services
* less 查看文件快速定位  
  /后面加正则向后查找；?后面加正则向前查找；n继续查找
* jps -lv查看Java进程信息
* cat 文件名  
  cat log.txt | grep 'ERROR' -A 5 之后5行  
  cat log.txt | grep 'ERROR' -B 5 之前5行  
  cat log.txt | grep 'ERROR' -C 5 前后5行

####git的一些常用命令
* git branch 创建分支
* git checkout -b  创建并切换
* git reflog  查看提交的历史版本
* git reset --hard 973cf21 回退到此版本