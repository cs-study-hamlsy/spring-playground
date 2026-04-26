# Contributing Guide

## Branch Strategy

- `experiment/{category}/{topic}`
- `setup/{description}`
- `fix/{description}`
- `docs/{description}`

## Commit Convention

```text
{type}({scope}): {description}
```

Types:

- `exp`
- `test`
- `docs`
- `setup`
- `fix`
- `chore`

## Experiment Workflow

1. Create issue from `.github/ISSUE_TEMPLATE/experiment.yml`
2. Create branch from `main`
3. Add/update module skeleton and tests
4. Document results in module README
5. Open PR with `.github/PULL_REQUEST_TEMPLATE.md`
