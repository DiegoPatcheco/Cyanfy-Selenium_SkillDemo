#!/usr/bin/env sh
set -eu

./mvnw --batch-mode --no-transfer-progress clean test \
  "-Dcucumber.filter.tags=@regression" \
  -Dbrowser=chrome \
  -DexecutionMode=local \
  -Dheadless
