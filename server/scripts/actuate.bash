#!/bin/bash

delay=0
value=0
valueArray=("off" "on")
unit=s
unitArray=("s" "m" "h")

function usage () { echo "Usage: actuate [--on | --off] [-v|--value 1|0] [-d|--delay DELAY] [-u|--unit s|m|h]"; }


VALID_ARGS=$(getopt -o v:d:u: --long on,off,delay:,value:,unit: -- "$@")
if [[ $? -ne 0 ]]; then
    usage
    exit 1;
fi

eval set -- "$VALID_ARGS"
while [ : ]; do
  case "$1" in
    -d | --delay)
        delay=$2
        shift 2
        ;;
    --on)
        value=1
        shift
        ;;
    --off)
        value=0
        shift
        ;;
    -v|--value)
        value=$2
        shift 2
        ;;
    -u|--unit)
        unit="$2"
        shift 2
        ;;
    --) shift; 
        break 
        ;;
  esac
done


if [ "$value" -ne 0 ] && [ "$value" -ne 1 ]; then
    usage;
    exit 1;
fi

for actuator in "$@"
do
    tdtool --${valueArray[$value]} $actuator
done

if [ "$delay" -gt 0 ]; then
    if [[ ! " ${unitArray[*]} " =~ " ${unit} " ]]; then
        usage;
        exit 1;
    fi
	(sleep "${delay}""$unit"; ./actuate.bash -v $((1-value)) "$@") & 
fi
