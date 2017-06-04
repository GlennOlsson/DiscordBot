#!/usr/bin/env bash
cd /home/pi/DiscordBot/DiscordBot/Discord || { echo 'cd failed' ; exit 1; }
git pull || { echo 'git pull failed' ; exit 1; }
sh compile.sh || { echo 'sh compile.sh failed' ; exit 1; }
tmux send-keys -t nvim Escape || { echo 'Could not exit running script in tmux'; exit 1; }
tmux attach || { echo 'tmux attach failed';}
sh rum.sh || { echo 'Could not run'; exit 1; }
echo 'Successfully reset DiscordBot'