---
name: seedu-git-standard
description: Git commit message and branch naming conventions for this project, based on the SE-EDU Git conventions guide (https://se-education.org/guides/conventions/git.html). Load before drafting any commit message or branch name in this repository.
---

# SE-EDU Git Conventions

This project follows the SE-EDU Git conventions for every commit and branch
in this repository.

Source: https://se-education.org/guides/conventions/git.html

## Commit message: subject line

- Limit the subject line to 50 characters where possible; 72 is a hard
  ceiling.
- Use the imperative mood: "Add README.md", not "Added README.md" or
  "Adding README.md".
- Capitalize the first letter: "Move index.html file to root", not
  "move index.html file to root".
- Do not end the subject line with a period: "Update sample data", not
  "Update sample data.".
- Optionally prefix with a scope/category when it aids scanning, e.g.
  `Person class: Remove static imports` or `bug fix: Add space after name`.

## Commit message: body

- Separate the subject from the body with one blank line.
- Wrap body text at 72 characters.
- Use blank lines to separate paragraphs; use bullet points where that's
  clearer than prose.
- Explain WHAT changed and WHY — not HOW (the diff already shows how).
- Rough template for the body:
  1. The current situation (present tense).
  2. Why it needs to change.
  3. What is being done (imperative mood).
  4. Why it's being done that way, plus any other context a reviewer needs
     to judge the change without re-reading the diff.
- Don't restate what an in-diff code comment already explains.
- If the body is getting long, that's a sign the commit should be split into
  smaller, finer-grained commits instead — one logical change per commit.

## Branch names

- Use meaningful, kebab-case names with relevant keywords, e.g.
  `refactor-ui-tests`.
- For issue-linked branches: `issueNumber-some-keywords-from-issue-title`,
  e.g. `1234-ui-freeze-error`.

## Applying this standard

- Draft every commit message against the rules above before running
  `git commit`.
- Keep commits atomic: one logical change per commit, so the subject/body
  can describe it without sprawling.
