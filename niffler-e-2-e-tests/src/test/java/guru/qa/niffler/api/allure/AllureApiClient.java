package guru.qa.niffler.api.allure;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.allure.AllureResults;
import guru.qa.niffler.model.allure.Project;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class AllureApiClient extends RestClient {

    private final AllureApi allureApi;

    public AllureApiClient() {
        super(CFG.allureServiceUrl());
        this.allureApi = retrofit.create(AllureApi.class);
    }

    public void createNewProject(String projectId) {
        final Response<Void> response;
        try {
            response = allureApi.projects(
                    new Project(projectId)
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    public void sendResult(String projectId, AllureResults allureResults) {
        final Response<Void> response;
        try {
            response = allureApi.sendResults(projectId, allureResults).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public void generateReport(String projectId, String executionName, String executionFrom, String executionType) {
        final Response<Void> response;
        try {
            response = allureApi.generateReport(
                    projectId,
                    executionName,
                    executionFrom,
                    executionType
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }
}
