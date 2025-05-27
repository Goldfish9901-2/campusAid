#!/usr/bin/env bash
# 请在bash环境下（linux自带sh/bash /macOS自带zsh /windows git自带bash）环境下运行
set -e
EXEC=""
[ "$OSTYPE" == "msys" ] && EXEC="./natapp.exe"
[ "$OSTYPE" == "cygwin" ] && EXEC="./natapp.exe"
[ "$OSTYPE" == "linux" ] && EXEC="./natapp"
[ "$EXEC" == "" ] && (echo "$OSTYPE is neither linux nor windows and we don't know how to run natapp.exe";false)
[ -f "./auth.txt" ] || (echo "auth.txt not found";false)
echo choose to execute $EXEC
${EXEC} "-authtoken=$(cat ./auth.txt)"
# gradle bootRun
