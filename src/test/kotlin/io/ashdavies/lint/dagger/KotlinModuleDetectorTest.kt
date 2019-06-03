package io.ashdavies.lint.dagger

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.jupiter.api.Test

internal class KotlinModuleDetectorTest {

  @Test
  fun `should detect non object module`() {
    val kls = """
      package io.ashdavies.lint.dagger

      @dagger.Module
      class DaggerModule {

        @Provides
        fun name() = "Hello World!"
      }
    """

    lint()
        .files(kotlin(kls).indented())
        .issues(KotlinModuleDetector.ISSUE)
        .run()
        .expect("")
  }
}
