package com.main.base.start;

import com.main.base.framework.CrudExamples;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Start {
    public static String zookeeperConnectionString = "192.168.3.140:2181";

    public static CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        return CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
    }

    public static void lock(CuratorFramework client, String lockPath) {
        TimeUnit waitUnit = TimeUnit.SECONDS;
        int maxWait = 5;
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        try {
            if (lock.acquire(maxWait, waitUnit)) {
                log.info("成功线程ID:{}", Thread.currentThread().getId());
//                lock.release();
            }else {
                log.info("失败线程ID:{}", Thread.currentThread().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void election(CuratorFramework client, String leaderPath){
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.info("I'm leader. Thread:{}",Thread.currentThread().getId());
            }
        };
        LeaderSelector selector = new LeaderSelector(client,leaderPath,listener);
        selector.start();
    }

    public static void main(String[] args) {
        CuratorFramework client = getCuratorFramework();
        client.start();
        String path = "/crd/file";
        String leaderPath = "/crd/leader";
        String testPath = "/crd/official";

        try {
            String result ;
//            CrudExamples.createEphemeral(client,testPath,"abd".getBytes());
            do {
                int i = ThreadLocalRandom.current().nextInt();
                CrudExamples.setData(client,testPath,String.valueOf(i).getBytes());
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                result = in.readLine();
            } while (StringUtils.isEmpty(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            ExecutorService service = Executors.newFixedThreadPool(10);
            for (int i = 1; i >= 0; i--){
                service.submit(() -> election(client, leaderPath));
            }
            service.shutdown();
            while (!service.isTerminated()){
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }*/

        /*try {
            curator.create().forPath(path, "a".getBytes());
            log.info("date:{}", curator.getData().forPath(path));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            curator.close();
        }*/
    }

}
