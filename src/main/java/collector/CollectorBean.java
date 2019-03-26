package collector;

import collector.metrics.BrandMetrics;
import collector.metrics.result.ProcessingHelper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
class CollectorBean {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorBean.class);

    private static final String METRICS_ENDPOINT = "http://localhost:8081/v1/metrics";
    private static final long MAX_DELAY = 300;


    List<BrandMetrics> collectMetrics() throws InterruptedException {
        final long startingPoint = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Queue<String> results = new ConcurrentLinkedQueue<>();
        while (!executorService.isShutdown()) {
            final long delay = System.currentTimeMillis() - startingPoint;
            LOG.info("While loop with delay: " + delay);
            executorService.submit(() -> executeCollectionRequest(delay, results, executorService));
            Thread.sleep(5);
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        LOG.info("queue size:" + results.size());
        List<BrandMetrics> brandMetrics = new ArrayList<>();
        Gson gson = new Gson();
        for(String result : results){
            brandMetrics.add(gson.fromJson(result, BrandMetrics.class));
            LOG.info("Parsing json list metrics" );
        }
        return brandMetrics;
    }

    private void executeCollectionRequest(final long delay, Queue<String> results, final ExecutorService executorService) {
        final long startPoint = System.currentTimeMillis() - delay;
        HttpResponse response;
        LOG.info("Delay." + delay);
            response = HttpHelper.executeRequest(METRICS_ENDPOINT);
            final long taken = System.currentTimeMillis() - startPoint;
            LOG.info("Taken:" + taken);
            if ((System.currentTimeMillis() - startPoint) > MAX_DELAY) {
                LOG.info("SR request takes too long.");
                executorService.shutdownNow();
                return;
            }
        assert response != null;
        int statusCode = response.getStatusLine().getStatusCode();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //collect data
            try {
                results.add(HttpHelper.streamTostring(response.getEntity().getContent()));
                LOG.info("Adding content to a queue");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.info("SR response status:" + statusCode);
    }


}