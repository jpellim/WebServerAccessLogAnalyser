# WebServerAccessLogAnalyser
Program to read log file, find ips that made a certain  requests number in a given period and persist to a mysql database.

## Getting Started
```
1 - Pull the project
2 - Open windows prompt command
3 - Access the project directory and then \dist
4 - Run java program from command line:
    java -cp "parser.jar" com.ef.Parser --accesslog={Downloaded directory}\WebServerAccessLogAnalyser\log\access.txt --startDate=2017-01-01.13:00:10 --duration=daily --threshold=300
```

### Prerequisites
* *Java 8*
* *MySQL Database*

### Test Files Directories
```
\WebServerAccessLogAnalyser\dist - executable jar file parser.jar
\WebServerAccessLogAnalyser\log - access log file.
\WebServerAccessLogAnalyser\deliverables - evidences files .png, SQLqueries and schema database.
```

## Author
* **Lucas Santiago**
