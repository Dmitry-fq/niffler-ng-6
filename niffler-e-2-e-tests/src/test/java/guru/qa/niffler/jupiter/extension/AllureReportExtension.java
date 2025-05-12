package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.allure.AllureApiClient;
import guru.qa.niffler.model.allure.AllureResult;
import guru.qa.niffler.model.allure.AllureResults;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class AllureReportExtension implements SuiteExtension {

    private static final boolean isDocker = "docker".equals(System.getProperty("test.env"));

    private static final AllureApiClient allureApiClient = new AllureApiClient();

    private static final String PROJECT_ID = "niffler-ng";

    private static final String ALLURE_RESULTS_DIRECTORY = "./niffler-e-2-e-tests/build/allure-results";

    @Override
    public void beforeSuite(ExtensionContext context) {
        if (isDocker) {
            allureApiClient.createNewProject(PROJECT_ID);
        }
    }

    @Override
    public void afterSuite() {
        if (isDocker) {
            try {
                allureApiClient.sendResult(PROJECT_ID, getAllureResults());
            } catch (IOException e) {
                throw new RuntimeException("Obtain results failed");
            }

            allureApiClient.generateReport(
                    PROJECT_ID,
                    System.getenv("HEAD_COMMIT_MESSAGE"),
                    System.getenv("BUILD_URL"),
                    System.getenv("EXECUTION_TYPE"));
        }
    }

    private AllureResults getAllureResults() throws IOException {
        final Base64.Encoder encoder = Base64.getEncoder();
        final List<AllureResult> results = new ArrayList<>();

        try (Stream<Path> filePathStream = Files.walk(Paths.get(ALLURE_RESULTS_DIRECTORY))) {
            filePathStream
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try (InputStream is = Files.newInputStream(filePath)) {
                            results.add(
                                    new AllureResult(
                                            filePath.getFileName().toString(),
                                            encoder.encodeToString(is.readAllBytes())
                                    )
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return new AllureResults(results);
    }
}
