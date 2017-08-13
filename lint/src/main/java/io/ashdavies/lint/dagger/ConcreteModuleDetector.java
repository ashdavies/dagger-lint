package io.ashdavies.lint.dagger;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.TextFormat;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import lombok.ast.AstVisitor;
import lombok.ast.ClassDeclaration;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.Node;

public class ConcreteModuleDetector extends Detector implements Detector.JavaScanner {

  private static final Implementation implementation = new Implementation(ConcreteModuleDetector.class, Scope.JAVA_FILE_SCOPE);

  private static final String DAGGER_MODULE_ANNOTATION = "dagger.Module";

  static final Issue ISSUE = Issue.create(
      "ConcreteModule",
      "Concrete Module Implementation",
      "Modules should be defined statically so that Dagger need not perform unnecessary object instantiation," +
          " additionally, this allows for usage of both @Binds and @Provides in the same module.",
      Category.PERFORMANCE,
      6,
      Severity.ERROR,
      implementation
  );

  @Override
  public boolean appliesTo(@NonNull Context context, @NonNull File file) {
    return true;
  }

  @Override
  public EnumSet<Scope> getApplicableFiles() {
    return Scope.JAVA_FILE_SCOPE;
  }

  @Override
  public List<Class<? extends Node>> getApplicableNodeTypes() {
    return Collections.singletonList(ClassDeclaration.class);
  }

  @Override
  public AstVisitor createJavaVisitor(@NonNull JavaContext context) {
    return new EnumChecker(context);
  }

  private static class EnumChecker extends ForwardingAstVisitor {

    private final JavaContext context;

    EnumChecker(JavaContext context) {
      this.context = context;
    }

    @Override
    public boolean visitClassDeclaration(ClassDeclaration declaration) {
      int flags = declaration.astModifiers().getEffectiveModifierFlags();
      if ((flags & Modifier.ABSTRACT) != 0) {
        return false;
      }

      /*JavaParser.ResolvedNode resolved = context.resolve(declaration);
      if (resolved.getAnnotation(DAGGER_MODULE_ANNOTATION) == null) {
        return false;
      }*/

      context.report(ISSUE, Location.create(context.file), ISSUE.getBriefDescription(TextFormat.TEXT));
      return super.visitClassDeclaration(declaration);
    }
  }
}
