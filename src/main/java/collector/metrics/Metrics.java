package collector.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Metrics {

    List<Metric> metrics = new ArrayList<>();
    long brandId;
    Date dateTime;
    String brandName;

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    void addMetric(List<Metric> incomingMetrics){
        metrics.addAll(incomingMetrics);
    }

    void addMetric(Metric metric){
        metrics.add(metric);
    }
}
