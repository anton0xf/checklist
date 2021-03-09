# checklist
simple checklist CLI app

## run
TODO how to pass args?
```shell
./gradlew run
```

## test
```shell
./gradlew test [-i]
```
It generates a [report](app/build/reports/tests/test/index.html).

## commands
TODO move to code and generate doc
### create
```shell
checklist create {name}
```
Creates (in the current directory) new checklist file named "{name}.checklist".
If {name} ends on ".checklist" then the repeat will be removed.

## file structure
TODO describe file structure