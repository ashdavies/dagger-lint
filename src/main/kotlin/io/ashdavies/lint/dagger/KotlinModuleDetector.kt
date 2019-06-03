package io.ashdavies.lint.dagger

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.visitor.AbstractUastVisitor

class KotlinModuleDetector : Detector(), Detector.UastScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UClass::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler = KotlinModuleHandler(context)

  private class KotlinModuleHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitClass(node: UClass) = node.accept(KotlinModuleAnnotationVisitor(context))
  }

  private class KotlinModuleAnnotationVisitor(private val context: JavaContext) : AbstractUastVisitor() {

    private var hasModuleAnnotation: Boolean = false

    override fun visitAnnotation(node: UAnnotation): Boolean {
      hasModuleAnnotation = node.qualifiedName == "dagger.Module"
      return true
    }

    override fun visitClass(node: UClass): Boolean {
      context.report(ISSUE, Location.create(context.file), "Found Dagger Module")
      return true
    }
  }

  companion object {

    val ISSUE: Issue = Issue.create(
        id = "KotlinModule",
        briefDescription = "Kotlin module implementation",
        explanation = "Kotlin modules should be defined as object",
        category = Category.PERFORMANCE,
        priority = 6,
        severity = Severity.WARNING,
        implementation = Implementation(KotlinModuleDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )
  }
}
