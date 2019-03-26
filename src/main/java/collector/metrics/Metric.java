package collector.metrics;

public class Metric {

    String metric;
    long count;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void updateCount(Metric metric){
        this.count += metric.getCount();
    }

}
