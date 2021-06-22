bank="."
java_solutions=../../../../..
java_advanced_2021=$java_solutions/../../java-advanced-2021
lib=$java_solutions/../lib

out=$bank/_build
mod_path=$java_advanced_2021/lib:$java_advanced_2021/artifacts:$lib

$bank/compile.sh $out $java_solutions "$mod_path"

java --module-path=$mod_path:$out -m info.kgeorgiy.ja.samsikova/info.kgeorgiy.ja.samsikova.bank.test.BankTests
echo Tests finished with exit code $?