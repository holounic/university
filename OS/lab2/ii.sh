#!/bin/bash

ps -Ao pid,command | awk '$2~/^\/sbin\//{print $1, $2}'