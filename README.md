# Open Platform Engineering

This project is on the way of to be a greate project to practicing Platform Engineering with Monorepo style.

## How to setup
1. Install Bazel
2. run `bazel build //...`
 
## How to develop
- pined maven_install.json: `bazel run @unpinned_maven//:pin`
- generate ddl for domain: `bazel run -- //tools/rules_ebean/src/main/java/internal:ebean_ddl -p=$PWD/repository-impl/src/main/sql -g="codes.showme.domain"`
- update pnpm-lock.yaml by: `bazel run  @pnpm//:pnpm --dir $PWD install --lockfile-only`
