package io.ashdavies.lint.dagger;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.TextFormat;
import com.android.utils.SdkUtils;
import com.google.common.collect.ObjectArrays;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;

public abstract class AbstractDetectorTest extends LintDetectorTest {

  private static final String PATH_DAGGER_LIBRARY = "libs/dagger.jar=>libs/dagger.jar";
  private static final String PATH_TEST_RESOURCES = "resources/test/";

  private static final String NO_WARNINGS = "No warnings.";

  @Override
  public InputStream getTestResource(String relativePath, boolean expectExists) {
    String path = (PATH_TEST_RESOURCES + relativePath).replace('/', File.separatorChar);
    File file = new File(getTestDataRootDir(), path);

    if (file.exists()) {
      try {
        return new BufferedInputStream(new FileInputStream(file));
      } catch (FileNotFoundException e) {
        if (expectExists) {
          fail("Could not find file " + file.getAbsolutePath());
        }
      }
    }

    return null;
  }

  private File getTestDataRootDir() {
    CodeSource source = getClass().getProtectionDomain().getCodeSource();

    if (source != null) {
      URL location = source.getLocation();
      try {
        File classesDir = SdkUtils.urlToFile(location);
        return classesDir.getParentFile().getAbsoluteFile().getParentFile().getParentFile();
      } catch (MalformedURLException e) {
        fail(e.getLocalizedMessage());
      }
    }

    return null;
  }

  String error(String name, Issue issue) {
    return String.format("%s: Error: %s [%s]\n1 errors, 0 warnings\n", name, issue.getBriefDescription(TextFormat.TEXT), issue.getId());
  }

  String noWarnings() {
    return NO_WARNINGS;
  }

  String project(String... path) throws Exception {
    return lintProject(ObjectArrays.concat(PATH_DAGGER_LIBRARY, path));
  }
}
