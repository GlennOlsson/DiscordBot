#!/usr/bin/env bash
cd /home/pi/DiscordBot/DiscordBot/Discord || { echo 'cd failed' ; exit 1; }
echo 'managed to cd'
git pull || { echo 'git pull failed' ; exit 1; }
echo 'managed to git pull'
sh compile.sh || { echo 'sh compile.sh failed' ; exit 1; }
echo 'managed to compile'
tmux send-keys -t 0 ^C || { echo 'Could not exit running script in tmux'; exit 1; }
echo 'managed to exit running tmux script'
tmux send-keys -t 0 sh run.sh Enter || { echo 'Could not run'; exit 1; }
echo 'managed send run and execute'
#tmux send-keys -t 0 Enter|| { echo 'Could not run'; exit 1; }
echo 'Successfully reset DiscordBot'