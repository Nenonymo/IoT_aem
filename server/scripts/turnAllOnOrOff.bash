for i in $(tdtool --list | grep E'^[0-9]+' | cut -f 1); 
do tdtool --$1 $i; 
done