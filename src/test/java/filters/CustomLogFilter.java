package filters;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomLogFilter {

    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private CustomLogFilter() {
    }

    public static CustomLogFilter customLogFilter() {
        return GetLogFilter.logFilter;
    }

    public AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;

    }

    private static class GetLogFilter {
        private static final CustomLogFilter logFilter = new CustomLogFilter();
    }
}
