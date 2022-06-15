/*
 * Copyright 2016 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.bugpatterns;

import com.google.errorprone.BugCheckerRefactoringTestHelper;
import com.google.errorprone.CompilationTestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link MustBeClosedChecker}. */
@RunWith(JUnit4.class)
public class MustBeClosedCheckerTest {

  private final BugCheckerRefactoringTestHelper refactoringHelper =
      BugCheckerRefactoringTestHelper.newInstance(MustBeClosedChecker.class, getClass());

  private final CompilationTestHelper compilationHelper =
      CompilationTestHelper.newInstance(MustBeClosedChecker.class, getClass());

  @Test
  public void positiveCases() {
    compilationHelper.addSourceFile("MustBeClosedCheckerPositiveCases.java").doTest();
  }

  @Test
  public void negativeCase() {
    compilationHelper.addSourceFile("MustBeClosedCheckerNegativeCases.java").doTest();
  }

  @Test
  public void refactoring() {
    refactoringHelper
        .addInput("MustBeClosedCheckerPositiveCases.java")
        .addOutput("MustBeClosedCheckerPositiveCases_expected.java")
        .allowBreakingChanges() // The fix is best-effort, and some variable names may clash
        .doTest();
  }

  @Test
  public void enumInitializer() {
    compilationHelper
        .addSourceLines(
            "Test.java",
            "import com.google.errorprone.annotations.MustBeClosed;",
            "import java.io.Closeable;",
            "enum Test {",
            "  A;",
            "  interface Foo extends Closeable {}",
            "  @MustBeClosed static Foo createResource() {",
            "    return null;",
            "  }",
            "  private final Foo resource;",
            "  private final Foo resource2 = createResource();",
            "  Test() {",
            "    this.resource = createResource();",
            "  }",
            "}")
        .doTest();
  }
}
