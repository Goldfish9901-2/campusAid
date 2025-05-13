# 请在bash环境下（linux自带sh/bash /macOS自带zsh /windows git自带bash）环境下运行
EXEC=./natapp
[ "$OSTYPE" == "msys" ] && EXEC=./natapp.exe
${EXEC} -authtoken=$(cat ./auth.txt) 
# gradle bootRun
