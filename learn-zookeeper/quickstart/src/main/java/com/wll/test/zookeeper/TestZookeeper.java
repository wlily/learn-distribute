package com.wll.test.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by wll on 17-7-7.
 */
public class TestZookeeper {

    public void test(){
        try {

            System.out.println("........");
            System.out.println("Starting to connectint zookeeper......");
            String connectString = "127.0.0.1:2181";
            int sessionTimeout = 2000;
            Watcher watcher = new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == null || "".equals(event.getType())) {
                        return;
                    }
                    System.out.println("已经触发了" + event.getType() + "事件!");
                }
            };
            ZooKeeper zk  = new ZooKeeper(connectString, sessionTimeout, watcher);
            System.out.println("Zookeeper Connect successfully");

            Thread.currentThread().sleep(10001);

            System.out.println(".......");
            System.out.println("开始创建根目录节点/tmp_root_path...");
            zk.exists("/tmp_root_path", true);
            zk.create("/tmp_root_path", "我是根目录节点/tmp_root_path".getBytes("utf-8"),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            System.out.println("根目录节点/tmp_root_path创建成功");
            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("开始创建第一个子目录节点/tmp_root_path/childPath1...");
            zk.exists("/tmp_root_path/childPath1", true);
            zk.create("/tmp_root_path/childPath1", "我是第一个子目录/tmp_root_path/childPath1".getBytes("utf-8"),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("第一个子目录节点/tmp_root_path/childPath1创建成功");
            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("开始修改第一个子目录节点/tmp_root_path/childPath1...");
            //通过getData 或 exists 方法触发watcher事件
            zk.getData("/tmp_root_path/childPath1", true, null);
            //version参数指定要更新的数据版本，如果version和真实的版本数据不同，则更新操作失败，
            // 当setData中设置版本为-1时，忽略版本检测
            zk.setData("/tmp_root_path/childPath1", "我是修改后第一个子目录/tmp_root_path/childPath1".getBytes("utf-8"), -1);
            System.out.println("修改第一个子目录节点/tmp_root_path/childPath1数据成功！");

            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("获取根目录节点状态......");
            System.out.println(zk.exists("/tmp_root_path", true));
            System.out.println("根目录节点获取成功");

            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("开始创建第二个子目录节点/tmp_root_path/childPath2...");
            zk.exists("/tmp_root_path/childPath2", true);
            zk.create("/tmp_root_path/childPath2", "我是第二个子目录节点/tmp_root_path/childPath2".getBytes("utf-8"),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("第二个子目录节点/tmp_root_path/childPath2创建成功");

            Thread.currentThread().sleep(10001);


            System.out.println("......");
            System.out.println("获取第二个子目录节点数据......");
            System.out.println(new String(zk.getData("/tmp_root_path/childPath2", true, null)));
            System.out.println("成功获取第二个子目录节点数据");


            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("开始获取根目录节点状态");
            System.out.println(zk.exists("/tmp_root_path", true));
            System.out.println("根目录节点状态获取成功");

            Thread.currentThread().sleep(10001);

            System.out.println("开始删除第一个子目录节点/tmp_root_path/childPath1...");
                         /*zk.getData("/tmp_root_path/childPath1", true, null);*/
            zk.exists("/tmp_root_path/childPath1", true);
            zk.delete("/tmp_root_path/childPath1", -1);
            System.out.println("第一个子目录节点/tmp_root_path/childPath1删除成功");

            Thread.currentThread().sleep(10001);

            System.out.println("开始获取根目录节点状态");
            System.out.println(zk.exists("/tmp_root_path", true));
            System.out.println("根目录节点状态获取成功");

            Thread.currentThread().sleep(10001);

            System.out.println("开始删除第二个子目录节点/tmp_root_path/childPath2......");
            zk.delete("/tmp_root_path/childPath2", -1);
            System.out.println("第二个子目录节点/tmp_root_path/childPath2删除成功");

            Thread.currentThread().sleep(10001);

            System.out.println("......");
            System.out.println("开始获取根目录节点状态");
            System.out.println(zk.exists("/tmp_root_path", true));
            System.out.println("根目录节点状态获取成功");

            Thread.currentThread().sleep(10001);

            System.out.println("开始删除根目录节点/tmp_root_path......");
            zk.delete("/tmp_root_path", -1);
            System.out.println("删除根目录节点/tmp_root_path成功");

            Thread.currentThread().sleep(10001);
            System.out.println("查看根目录节点状态");
            System.out.println(zk.exists("/tmp_root_path", true));
            System.out.println("根目录节点状态获取成功");

            Thread.currentThread().sleep(10001);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
//
//        　在代码中，我们可以看到zk.exists("****",true)，不断出现，目的是为了监听相应的znode 修改和删除事件，
//        从结果中我们也不难看出监听结果“已经触发了NodeDataChanged事件！”和“已经触发了NodeDeleted事件！”。
//        之所以要对每个节点多次执行zk.exists()，这是因为在zookeeper机制下，
//        zk.exists()方法、zk.get()方法和zk.getChildren()方法仅仅监控对应节点的一次变化（数据变化或者子节点数目发生变化）。

//　　zookeeper可以监控到的事件类型：//
//        ZOO_CREATED_EVENT:节点创建事件，需要watch一个不存在的节点，当此节点被创建时，通过exist()设置可以触发该对该事件的监控；
//        ZOO_DELETED_EVENT:节点删除事件，此watch通过exists()或者get()设置监控；
//        ZOO_CHANGED_EVENT:节点数据改变事件，此watch通过exists()或者get()设置监控；
//        ZOO_CHILD_EVENT:子节点列表改变事件，此watch通过getChildren()设置监控；
//        ZOO_SESSION_EVENT:会话失效事件，客户端与服务端断开或者重新连结时触发；
//        ZOO_NOWATCHING_EVENT:watch移除事件，服务端因为某些原因不再为客户端watch节点的触发
    }

    public static void main(String[] args) {
        new TestZookeeper().test();
    }
}
