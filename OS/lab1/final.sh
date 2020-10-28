#!/bin/bash

binbashs=`grep -n '#!/bin/bash$' $1 | cut -d -f 1`
sed -in-place -Er 's/#.*//' $1

if [[  ! -z $binbashs ]]
then
sed -Ei '' '1i\ 
#!/bin/bash
' $1
fi