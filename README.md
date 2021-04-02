# checklist
simple checklist CLI app

## run
See [Gradle Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html).

To run using gradle use `run` task. Pass args using `--args` option, e.g.
```shell
$ ./gradlew run --args "create test"
> Checklist 'test.checklist' created
```
Or build jar and run it:
```shell
$ ./gradlew jar
$ java -cp app/build/libs/app.jar checklist.App create test
```
Or create distribution (also use `./gradlew dist{Zip,Tar}`), extract it and run generated script.
Or create same application image in `build/install` and run generated script: 
```shell
$ ./gradlew installDist
$ ./app/build/install/app/bin/app create test
```
 Or create and run fat-jar:
```shell
$ ./gradlew shadowRun
$ java -jar app/build/libs/app-all.jar create test
```

## test
```shell
$ ./gradlew test [-i]
```
It generates a test report (in `app/build/reports/tests/test/index.html`) 
and coverage report (in `app/build/reports/jacoco/test/html/index.html`).

## commands
TODO move to code and generate doc
### create
```shell
$ checklist create {name}
```
Creates (in the current directory) new checklist file named "{name}.checklist".
If {name} ends on ".checklist" then the repeat will be removed.

## file structure
TODO describe file structure