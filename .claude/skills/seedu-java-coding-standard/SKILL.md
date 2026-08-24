---
name: seedu-java-coding-standard
description: Java coding conventions for this project, based on the SE-EDU intermediate Java standard (https://se-education.org/guides/conventions/java/intermediate.html). Load before writing or reviewing any Java code in this repository — naming, layout, statements, and comment/Javadoc rules.
---

# SE-EDU Intermediate Java Coding Standard

This project follows the SE-EDU intermediate Java coding standard for all Java
source under `src/`. Apply these rules whenever writing new code, editing
existing code, or reviewing a diff.

Source: https://se-education.org/guides/conventions/java/intermediate.html

## Naming

- **Packages**: all lower case (e.g. `lebron.task`).
- **Classes/enums**: nouns in `PascalCase` (e.g. `Deadline`, `TaskList`).
- **Variables**: `camelCase` (e.g. `taskCount`).
- **Constants** (`static final`): `SCREAMING_SNAKE_CASE`, e.g. `MAX_ITERATIONS`.
  Associated constants share a common prefix, e.g. `COLOR_RED`, `COLOR_GREEN`.
- **Methods**: verbs in `camelCase` (e.g. `getName()`, `computeTotalWidth()`).
- **Test methods**: `featureUnderTest_testScenario_expectedBehavior()`, e.g.
  `sortList_emptyList_exceptionThrown()`. The scenario and/or expected-behavior
  parts may be dropped when not needed.
- **Abbreviations/acronyms** are not all-caps inside a name:
  `exportHtmlSource()`, not `exportHTMLSource()`.
- All names are written in English.
- Variable name length should match scope: short-lived scratch/index
  variables (`i`, `j`, `k`, `c`, `d`) can be short; long-lived or
  widely-scoped variables need descriptive names.
- Iterator variables: `i`, then `j`, `k`, ... for nested loops.
- **Booleans** read like yes/no questions: `isSet`, `hasData`, `wasOpen`,
  `hasLicense()`, `canEvaluate()`. Setter form: `setFound(boolean isFound)`.
- **Collections** use plural names: `List<Task> tasks;`, `int[] values;`.

## Layout

- Indentation: 4 spaces, never tabs.
- Line length: soft limit < 110 chars, hard limit 120 chars.
- Wrapped lines: indent continuation by 8 spaces (double the normal indent).
- Break after a comma; break *before* an operator (including `.`).
- A method/constructor name stays attached to the `(` that follows it — never
  break the line between the name and the opening parenthesis.
- Prefer higher-level breaks (around the lowest-precedence operator) over
  low-level breaks.
- Brackets are K&R / Egyptian style — opening brace on the same line:
  ```java
  while (!done) {
      doSomething();
  }
  ```
- `switch` fallthrough cases must carry an explicit `// Fallthrough` comment.
- Whitespace: spaces around binary operators and keywords (`while (true) {`),
  after commas, and around `for`-loop semicolons. No space before `(` in a
  method call; no space between a method name and its `(`.
- Separate logical units within a block with exactly one blank line, and
  precede each unit with a one-line comment when it clarifies intent.

## Statements

- Every class belongs to a package (no default/unnamed package).
- Import order, each group separated by a blank line: static imports →
  `java.*` → `javax.*` → other organizational (`org.*`) → third-party
  (`com.*`) → project-specific.
- Import classes explicitly; never use a wildcard (`import java.util.*;`).
- Array specifiers attach to the type, not the variable: `int[] a`, not
  `int a[]`.
- Declare and initialize variables at the point of use, in the smallest
  scope that works — not all up front.
- Class fields are never `public` unless the class is a pure data class with
  no behavior (constants are exempt from this rule).
- Loop and conditional bodies are always wrapped in `{ }`, even for a single
  statement, and the condition/loop header is never on the same line as the
  body statement.

## Comments and Javadoc

- All comments are written in English (American spelling).
- Every public class and public method needs a descriptive header comment.
  Getters/setters, unmodified `@Override`s, and test code may omit it.
- Javadoc format:
  ```java
  /**
   * Returns lateral location of the specified position.
   * If the position is unset, NaN is returned.
   *
   * @param x X coordinate of position.
   * @param y Y coordinate of position.
   * @return Lateral location.
   * @throws IllegalArgumentException If zone is <= 0.
   */
  public double computeLocation(double x, double y) throws IllegalArgumentException {
      // ...
  }
  ```
  - First sentence is a short summary starting with a third-person verb:
    "Returns ...", "Sends ...", "Adds ..." — not imperative ("Return ...").
  - Blank line between the description and the `@param`/`@return`/`@throws`
    block; each tag's description ends with a period.
  - `@return` may be dropped for `void` methods or when the return value is
    obvious; `@param` may be dropped only when *all* parameters are already
    self-explanatory or covered in the summary (all-or-nothing).
  - Overridden methods that only refine the parent's contract use
    `{@inheritDoc}`.
  - A single-line field comment is fine: `/** Number of connections to this database */`.
- Comment indentation matches the code it documents; trailing comments are
  allowed (`process('ABC'); // process a dummy String first`).

## Applying this standard

- New code must comply from the start — do not write violations and fix them
  later.
- When editing a file for another reason, fix standard violations you touch
  in the same area, but avoid unrelated drive-by reformatting of code you
  aren't otherwise changing.
