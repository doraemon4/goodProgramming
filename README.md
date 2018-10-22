# goodProgramming
积累工作中使用的一些常用技巧  

### 多线程
*  **synchronized**锁住的是对象，是防止多个线程同时操作同一个对象的同步代码块，对于方法上面的**synchronized**锁住的就是当前对象*this*
*  避免一个线程同时获取多个锁  
    `synchronized(B){
        synchronized (A) {
            System.out.println("2");
        }
    }`
*  避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
*  尝试使用定时锁`lock.tryLock(timeout)`来代替使用内部锁机制
*  **volatile**实现了多个线程之间共享，体现了可见性，但是却不能实现原子性。`java.util.concurrent.atomic`下的AtomicInteger可实现原子性
![Image text](https://github.com/doraemon4/goodProgramming/blob/master/png/muti_thread_safe.png)
*  `executorService.shutdown()`并不会马上关闭线程池，会等到所有的线程执行完，才会关闭。`executorService.shutdownNow()`马上会关闭线程池，
正在运行的线程会被强制中断。正确的关闭方式：

        try{
            //发出关闭线程池的通知
            executorService.shutdown();
            //判断是否超过了超时时间
            if(!executorService.awaitTermination(waitTime,TimeUnit.SECONDS)){
                //如果超时，线程强制中断
                executorService.shutdownNow();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
*  exchanger实现两个线程之间交换交换数据，如果没有其他线程交换，将一直阻塞下去。
![Image text](https://github.com/doraemon4/goodProgramming/blob/master/png/exchanger.png)
### linux一些常用的命令:
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

### git的一些常用命令
* git branch 创建分支
* git checkout -b  创建并切换
* git reflog  查看提交的历史版本
* git reset --hard 973cf21 回退到此版本
* git push -f 强制推(遇到代码提交不上去，又merge不了，强制用本地代码覆盖git仓库的代码)
