#!/bin/bash
sleep 15
mv /home/evelyn/Running/Evelyn.jar /home/evelyn/Running/Evelyn.old
cp /home/evelyn/DiscordBot/target/DiscordBot-1.0.0.jar /home/evelyn/Running/Evelyn.jar
screen -r -S TestBot
cd /home/evelyn/Running/
java -jar Evelyn.jar