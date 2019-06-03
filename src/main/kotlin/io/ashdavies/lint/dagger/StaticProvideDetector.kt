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
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.AbstractUastVisitor

class StaticProvideDetector : Detector(), Detector.UastScanner {

  override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UMethod::class.java)

  override fun createUastHandler(context: JavaContext): UElementHandler = StaticProvideHandler(context)

  private class StaticProvideHandler(private val context: JavaContext) : UElementHandler() {
    override fun visitMethod(node: UMethod) = node.accept(StaticProvideVisitor(context))
  }

  private class StaticProvideVisitor(private val context: JavaContext) : AbstractUastVisitor() {

    private var hasJvmStaticAnnotation: Boolean = false
    private var hasProvideAnnotation: Boolean = false

    override fun visitAnnotation(node: UAnnotation): Boolean {
      when {
        node.qualifiedName == "JvmStatic" -> hasJvmStaticAnnotation = true
        node.qualifiedName == "dagger.Provides" -> hasProvideAnnotation = true
        else -> return super.visitAnnotation(node)
      }
      return true
    }

    override fun visitMethod(node: UMethod): Boolean {
      if (hasProvideAnnotation && !hasJvmStaticAnnotation) {
        context.report(ISSUE, Location.create(context.file), "Non static Dagger provision")
        return true
      }

      return false
    }
  }

  companion object {

    val ISSUE: Issue = Issue.create(
        id = "StaticProvide",
        briefDescription = "Value provided from non-static method",
        explanation = "Dagger provide methods should be defined statically",
        category = Category.PERFORMANCE,
        priority = 6,
        severity = Severity.WARNING,
        implementation = Implementation(StaticProvideDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )
  }
}
