out=$1
java_solutions=$2
mod_path=$3

package=$java_solutions/info/kgeorgiy/ja/samsikova
bank=$java_solutions/info/kgeorgiy/ja/samsikova/bank

rm -r $out
mkdir $out
find $java_solutions -name "*.java" > source.txt
javac --module-path $mod_path -d $out @source.txt
rm source.txt


