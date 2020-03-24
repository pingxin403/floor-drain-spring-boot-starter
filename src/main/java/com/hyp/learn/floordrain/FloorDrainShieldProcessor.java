package com.hyp.learn.floordrain;

import com.hyp.learn.floordrain.cache.Cache;
import com.hyp.learn.floordrain.cache.CacheObject;
import com.hyp.learn.floordrain.property.FloorDrainProperties;
import com.hyp.learn.floordrain.utils.GlobalFloorDrainUtil;
import com.hyp.learn.floordrain.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author hyp
 * Project name is floor-drain-spring-boot-starter
 * Include in com.hyp.learn.floordrain
 * hyp create at 20-3-24
 **/
public class FloorDrainShieldProcessor implements FloorDrainProcessor {
    private static final Logger log = LoggerFactory.getLogger(FloorDrainShieldProcessor.class);

    @Autowired
    private FloorDrainProperties properties;
    @Autowired
    @Qualifier("floorDrainCache")
    private Cache cache;

    /**
     * 布隆过滤器
     *
     * @param request
     * @return
     */
    @Override
    public FloorDrainResponse process(HttpServletRequest request) {
        RequestUtil requestUtil = new RequestUtil(request);
        String ip = requestUtil.getIp();
        String key = GlobalFloorDrainUtil.INSTANCE.formatKey(ip);
        String lockKey = GlobalFloorDrainUtil.INSTANCE.getLockKey(key);
        String lockMsgTpl = "[%s]涉嫌恶意访问已被临时限制！共被限制过[%s]次，本次剩余限制时间:%s ms",
                accessMsgTpl = "[%s]在%s毫秒内已连续发起 %s 次请求";

        int blocklistCount = 0;
        boolean isBlockingAccess = (properties.getFaultTolerance() != -1 && (blocklistCount = this.getBlacklistCount(key)) > properties.getFaultTolerance());
        if (isBlockingAccess) {
            return new FloorDrainResponse().isError()
                    .setMsg("Access has been restricted!")
                    .setExpire(-1)
                    .setLimitCount(blocklistCount)
                    .setAccessInfo(request);
        }
        // 本次被限制访问
        if (cache.hasKey(lockKey)) {
            blocklistCount = this.getBlacklistCount(key);
            long expire = cache.getExpire(lockKey);
            String msg = String.format(lockMsgTpl, ip, blocklistCount, expire);
            log.debug(msg);
            return new FloorDrainResponse().isError()
                    .setMsg(msg)
                    .setExpire(expire)
                    .setLimitCount(blocklistCount)
                    .setAccessInfo(request);
        }

        // 再次被限制访问
        if (isLimitedAccess(key)) {
            this.save2Blacklist(key);
            cache.set(lockKey, 1, properties.getLimitedTime(), TimeUnit.MILLISECONDS);
            long expire = cache.getExpire(lockKey);
            blocklistCount = this.getBlacklistCount(key);
            String msg = String.format(lockMsgTpl, ip, blocklistCount, expire);
            return new FloorDrainResponse().isError()
                    .setMsg(msg)
                    .setExpire(expire)
                    .setLimitCount(blocklistCount)
                    .setAccessInfo(request);
        }

        String msg = String.format(accessMsgTpl, ip, properties.getInterval(), cache.get(key).getValue());
        return new FloorDrainResponse()
                .isSuccess()
                .setMsg(msg)
                .setAccessInfo(request);
    }


    /**
     * 拉黑用户
     *
     * @param ip 当前发起请求的用户IP
     */
    private void save2Blacklist(String ip) {
        String blacklistKey = GlobalFloorDrainUtil.INSTANCE.getBlacklistKey(ip);
        cache.set(blacklistKey, getBlacklistCount(ip) + 1, properties.getBlacklistTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取被拉黑过的次数
     *
     * @param ip 当前发起请求的用户IP
     */
    private int getBlacklistCount(String ip) {
        String blacklistKey = GlobalFloorDrainUtil.INSTANCE.getBlacklistKey(ip);
        CacheObject blacklistCache = cache.get(blacklistKey);
        if (null == blacklistCache) {
            return 0;
        }
        return blacklistCache.getValue();
    }

    /**
     * 是否已经被限制访问
     *
     * @param ip 当前发起请求的用户IP
     */
    private boolean isLimitedAccess(String ip) {
        return cache.incrementAndGet(ip) > properties.getThreshold();
    }
}
