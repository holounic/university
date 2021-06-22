bank="."
java_solutions=../../../../..
java_advanced_2021=$java_solutions/../../java-advanced-2021
lib=$java_solutions/../lib
standalone=$java_solutions/../standalone

out=$bank/_build
mod_path=$java_advanced_2021/lib:$java_advanced_2021/artifacts:$lib

junit_standalone_jar=$standalone/junit-platform-console-standalone-1.6.1.jar

$bank/compile.sh $out $java_solutions "$mod_path"

java -jar $junit_standalone_jar --class-path $out --scan-class-path
echo Tests finished with exit code $?
