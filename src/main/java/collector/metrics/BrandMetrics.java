package collector.metrics;

import java.util.List;

public class BrandMetrics {

    List<Metrics> brandMetrics;
    long size;

    public List<Metrics> getBrandMetrics() {
        return brandMetrics;
    }

    public void setBrandMetrics(List<Metrics> brandMetrics) {
        this.brandMetrics = brandMetrics;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long countImpressions(){
        long count = 0;
        for(Metrics mts : brandMetrics){
            for(Metric m : mts.getMetrics()){
                count = m.getCount();
            }
        }
        return count;
    }
}
