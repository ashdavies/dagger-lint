package io.ashdavies.lint.dagger;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.TextFormat;
import java.util.Collections;
import java.util.List;

public class ConcreteModuleDetectorTest extends AbstractDetectorTest {

  @Override
  protected Detector getDetector() {
    return new ConcreteModuleDetector();
  }

  @Override
  protected List<Issue> getIssues() {
    return Collections.singletonList(ConcreteModuleDetector.ISSUE);
  }

  public void testAbstractModuleDetection() throws Exception {
    String actual = lintProject(
        "Beta.java=>src/test/dagger/Beta.java",
        "Module.java=>src/test/dagger/Module.java",
        "AbstractModuleTestCase.java=>src/test/io/ashdavies/dagger/lint/AbstractModuleTestCase.java"
    );

    assertEquals(noWarnings(), actual);
  }

  public void testConcreteModuleDetector() throws Exception {
    String expected = "src/test/io/ashdavies/dagger/lint/ConcreteModuleTestCase.java: Error: "
        + ConcreteModuleDetector.ISSUE.getBriefDescription(TextFormat.TEXT)
        + " ["
        + ConcreteModuleDetector.ISSUE.getId()
        + "]\n"
        + "1 errors, 0 warnings\n";

    String actual = lintProject(
        "Beta.java=>src/test/dagger/Beta.java",
        "Module.java=>src/test/dagger/Module.java",
        "ConcreteModuleTestCase.java=>src/test/io/ashdavies/dagger/lint/ConcreteModuleTestCase.java"
    );

    assertEquals(expected, actual);
  }
}
