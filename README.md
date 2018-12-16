# cgccli
> CGC CLI Tool

In order to build the project you need Maven and JDK 8 or higher installed.
## Build command
```sh
mvn clean install
```
## Running
```sh
java -jar <JAR_NAME> <command>
```



## Commands

### List projects
Lists all projects owned by user
```sh
--token <authentication_token> projects list
```

### List files
Lists all files in given project
```sh
--token <authentication_token> files list --project <project_id>
```

### Get file details
Retrieves details of a given file
```sh
--token <authentication_token> files stat --file <file_id>
```

### Update file details
Updates details of a given file.
```sh
--token <authentication_token> files update --file <file_id> [--<property>=<value>]*
```
_Example: --token bde920 files update --file e4b03c --name=foobar --metadata.foo=bar --metadata.bar=foo_


### Download file
Downloads file
```sh
--token <authentication_token> files download --file <file_id> --dest <destination_path>
```
