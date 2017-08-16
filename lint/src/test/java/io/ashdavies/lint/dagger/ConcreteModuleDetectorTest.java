package io.ashdavies.lint.dagger;

import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

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
    assertThat(project("AbstractModuleTestCase.java=>src/test/io/ashdavies/lint/dagger/AbstractModuleTestCase.java"))
        .isEqualTo(noWarnings());
  }

  public void testConcreteModuleDetector() throws Exception {
    assertThat(project("ConcreteModuleTestCase.java=>src/test/io/ashdavies/lint/dagger/ConcreteModuleTestCase.java"))
        .isEqualTo(error("src/test/io/ashdavies/lint/dagger/ConcreteModuleTestCase.java", ConcreteModuleDetector.ISSUE));
  }
}
