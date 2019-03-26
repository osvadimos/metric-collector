package collector;

import collector.metrics.BrandMetrics;
import collector.metrics.result.ProcessingHelper;
import com.google.gson.Gson;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller("/")
public class IndexController {

    @Inject
    private CollectorBean collectorBean;

    @Get("/")
    public MutableHttpResponse<String> index() {
        List<BrandMetrics> result = new ArrayList<>();
        Gson gson = new Gson();
        try {
            result = collectorBean.collectMetrics();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return HttpResponse.ok(gson.toJson(ProcessingHelper.generateResult(result)));
    }

    @Get("/second")
    public MutableHttpResponse<String> second() {
        List<BrandMetrics> result = new ArrayList<>();
        Gson gson = new Gson();
        try {
            result = collectorBean.collectMetrics();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return HttpResponse.ok(gson.toJson(ProcessingHelper.generateResult(result)));
    }
}