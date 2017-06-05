#!/usr/bin/env bash
echo 'Trying to cd'
cd /home/pi/DiscordBot/DiscordBot/Discord || { echo 'cd failed' ; exit 1; }
echo 'Managed to cd, trying to git pull'
git pull || { echo 'git pull failed' ; exit 1; }
echo 'Managed to git pull, trying to compile'
sh compile.sh || { echo 'sh compile.sh failed' ; exit 1; }
echo 'Managed to compile, trying to exit running tmux'
tmux send-keys -t 0 ^C || { echo 'Could not exit running script in tmux'; exit 1; }
echo 'Managed to exit running tmux script, trying to run and execute'
tmux send-keys -t 0 'sh run.sh' Enter || { echo 'Could not run'; exit 1; }
echo 'Managed send run and execute'
#tmux send-keys -t 0 Enter|| { echo 'Could not run'; exit 1; }
echo 'Successfully reset DiscordBot, will try to kill temporary tmux'
tmux kill-session -t "temp" && echo 'Temp tmux is terminated'