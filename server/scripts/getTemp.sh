#!/bin/bash

tdtool --list-sensors | grep -o -E 'temperature=[^ ]+' | cut -c 13-16