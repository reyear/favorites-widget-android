#!/usr/bin/env sh
set -eu
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec "$APP_HOME/gradle-8.7/bin/gradle" "$@"
