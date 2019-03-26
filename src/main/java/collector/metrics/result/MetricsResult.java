package collector.metrics.result;

import collector.HttpHelper;
import collector.metrics.Metric;
import collector.metrics.Metrics;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

class MetricsResult {

    private List<MetricResult> metricResultList = new ArrayList<>();

    void addMetrics(Metrics metrics) {
        OptionalInt index = IntStream.range(0, metricResultList.size())
                .filter(idx -> metricResultList.get(idx).getId() == metrics.getBrandId())
                .findFirst();

        MetricResult metricResult = new MetricResult();
        if (!index.isPresent()) {
            metricResult.setId(metrics.getBrandId());
            metricResult.setName(metrics.getBrandName());
            metricResult.setMetrics(metrics.getMetrics());
            metricResultList.add(metricResult);
        } else {
            metricResult = metricResultList.get(index.getAsInt());
            metricResult.updateMetricValues(metrics);
        }
    }

    public List<MetricResult> getMetricResultList(){
        return this.metricResultList;
    }

    void sortMetricResultListByImpressionCount(){
        metricResultList.sort((o1, o2) -> o2.countImpressions().compareTo(o1.countImpressions()));
    }

}

class MetricResult {


    long id;
    String name;
    List<Metric> metrics = new ArrayList<>();

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        if(name != null){
            this.name = name;
            return;
        }
        HttpResponse response = HttpHelper.executeRequest("http://localhost:8081/v1/brands/" + id);
        assert response != null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //collect data
            try {
                String result = HttpHelper.streamTostring(response.getEntity().getContent());
                Gson gson = new Gson();
                Brand b = gson.fromJson(result, Brand.class);
                this.name = b.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    void updateMetricValues(Metrics inboundMetrics) {

        for (Metric inboundMetric : inboundMetrics.getMetrics()) {
            OptionalInt index = IntStream.range(0, metrics.size())
                    .filter(idx -> metrics.get(idx).getMetric().equals(inboundMetric.getMetric()))
                    .findFirst();

            if (index.isPresent()) {
                Metric metric = metrics.get(index.getAsInt());
                metric.updateCount(inboundMetric);
            } else {
                metrics.add(inboundMetric);
            }
        }
    }

    public Long countImpressions(){
        long count = 0;
        for(Metric mts : metrics){
            if(mts.getMetric().equals("impression"))
                count = mts.getCount();
            }
        return count;
    }


}
