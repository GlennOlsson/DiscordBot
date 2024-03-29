#!/usr/bin/env bash
echo 'Trying to cd'
cd /home/pi/DiscordBot || { echo 'cd failed' ; exit 1; }
echo 'Managed to cd, trying to git pull'
git pull || { echo 'git pull failed' ; exit 1; }
echo 'Managed to git pull, trying create new tmux'
tmux new -s DiscordBot || { echo 'creating new tmux failed' ; exit 1; }
echo 'Managed to create new tmux, trying to cd in tmux'
tmux send-keys -t DiscordBot 'cd /home/pi/DiscordBot' Enter || { echo 'Could not cd in tmux'; exit 1; }
echo 'Managed to cd in tmux, trying to run'
tmux send-keys -t DiscordBot 'gradle run' Enter || { echo 'Could not run'; exit 1; }
echo 'Managed send run and execute'
echo 'Successfully started DiscordBot'
