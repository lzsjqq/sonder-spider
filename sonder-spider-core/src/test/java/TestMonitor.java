import com.xyc.spider.core.base.SpiderConfig;
import com.xyc.spider.core.bean.Spider;
import com.xyc.spider.core.bean.SpiderType;
import com.xyc.spider.core.monitor.ZookeeperSpiderMonitor;

/**
 * @description:
 * @author: xyc
 * @create: 2019-02-03 17:32
 */
public class TestMonitor {

    public static void main(String args[]) {
        try {
            ZookeeperSpiderMonitor spiderMonitor = ZookeeperSpiderMonitor.getInstance(SpiderConfig.getInstance());
            Spider spider = new Spider();
            spider.setSpiderType(SpiderType.DEFAULT);
            spiderMonitor.register(spider);
            spiderMonitor.getAllSpiders(SpiderType.DEFAULT);
            while (true) {
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
