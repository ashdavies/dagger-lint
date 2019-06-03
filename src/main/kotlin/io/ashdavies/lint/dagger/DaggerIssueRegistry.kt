package io.ashdavies.lint.dagger

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class DaggerIssueRegistry : IssueRegistry() {

  override val issues: List<Issue> = listOf(KotlinModuleDetector.ISSUE)
}
