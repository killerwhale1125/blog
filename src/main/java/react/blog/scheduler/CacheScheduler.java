package react.blog.scheduler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {
    private final CacheManager cacheManager;

    public CacheScheduler(@Qualifier("redisCacheManager") CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 매주 월요일 자정(00:00:00)에 메서드가 실행되도록 설정
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void clearWeeklyTopPostsCache() {
        cacheManager.getCache("topBoardsCache").clear();
    }
}
