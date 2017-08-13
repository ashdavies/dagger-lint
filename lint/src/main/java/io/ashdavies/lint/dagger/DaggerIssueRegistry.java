package io.ashdavies.lint.dagger;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class DaggerIssueRegistry extends IssueRegistry {

  private static final List<Issue> issues;

  static {
    List<Issue> list = new ArrayList<>();

    list.add(ConcreteModuleDetector.ISSUE);

    issues = Collections.unmodifiableList(list);
  }

  @Override
  public List<Issue> getIssues() {
    return issues;
  }
}
