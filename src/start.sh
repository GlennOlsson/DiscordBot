#!/usr/bin/env bash
echo 'Trying to cd'
cd /home/pi/DiscordBot/DiscordBot/Discord || { echo 'cd failed' ; exit 1; }
echo 'Managed to cd, trying to git pull'
git pull || { echo 'git pull failed' ; exit 1; }
echo 'Managed to git pull, trying to compile'
sh compile.sh || { echo 'sh compile.sh failed' ; exit 1; }
echo 'Managed to compile, trying create new tmux'
tmux || { echo 'creating new tmux failed' ; exit 1; }
echo 'Managed to create new tmux, trying to run and execute'
tmux send-keys -t 0 'sh run.sh' Enter || { echo 'Could not run'; exit 1; }
echo 'Managed send run and execute'
echo 'Successfully reset DiscordBot'