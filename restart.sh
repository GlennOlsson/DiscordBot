#!/usr/bin/env bash
echo 'Trying to cd'
cd /home/tau/DiscordBot/DiscordBot || { echo 'cd failed' ; exit 1; }
echo 'Managed to cd, trying to git pull'
git pull || { echo 'git pull failed' ; exit 1; }
echo 'Managed to git pull, trying to exit running tmux'
tmux send-keys -t DiscordBot ^C || { echo 'Could not exit running script in tmux'; exit 1; }
echo 'Managed to exit running tmux script, trying to sleep for 5 seconds'
sleep 5
echo 'Done sleeping, trying to run and execute'
tmux send-keys -t DiscordBot 'gradle run' Enter || { echo 'Could not run'; exit 1; }
echo 'Managed send run and execute'
#tmux send-keys -t 0 Enter|| { echo 'Could not run'; exit 1; }
echo 'Successfully reset DiscordBot'
#echo 'Eill try to kill temporary tmux'
#tmux kill-session -t "temp" && echo 'Temp tmux is terminated'