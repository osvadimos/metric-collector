package collector.metrics.result;

import collector.metrics.BrandMetrics;
import collector.metrics.Metrics;

import java.util.List;

public class ProcessingHelper {

    public static List generateResult(List<BrandMetrics> brandMetricsList) {
        MetricsResult metricsResult = collectMetrics(brandMetricsList);
        metricsResult.sortMetricResultListByImpressionCount();
        return metricsResult.getMetricResultList();
    }


    private static MetricsResult collectMetrics(List<BrandMetrics> brandMetricsList){
        MetricsResult metricsResult = new MetricsResult();
        for (BrandMetrics brandMetrics : brandMetricsList) {
            for (Metrics metrics : brandMetrics.getBrandMetrics()) {
                metricsResult.addMetrics(metrics);
            }
        }
    }

}
