# Spark Web API

A small plugin which will host a Web API for Spark on 
port 3000.

This plugin is made for Paper. You are free to fork and 
port this to other mod loaders.

This plugin uses Javalin library for hosting the web API.

## Installation
First, Install Spark to your Paper server.

Then, Install Spark Web API.
There are two files you could download, which are:
- spark-web-api-1.0-SNAPSHOT.jar
- spark-web-api-1.0-SNAPSHOT-all.jar

The `spark-web-api-1.0-SNAPSHOT.jar` is a JAR file without 
Javalin. You'll have to somehow add Javalin to classpath at 
runtime.

The `spark-web-api-1.0-SNAPSHOT-all.jar` is a JAR file with 
Javalin built-in. It is recommended that you download this 
file as it is easier to set up.

## Building
To build the Spark Web API, You'll have to run the shadowJar 
task to build the JAR with Javalin built-in. The JAR output 
can be found at `/build/libs/spark-web-api-version-all.jar`.

This plugin will eventually use the custom plugin loader 
feature of Paper once it has become more usable and stable.

## API paths
### /api/tps
Return the TPS of the past 10 seconds, 1 minute, 
5 minutes, and 15 minutes.

Example Response:
```json
{
  "tenSeconds": 19.999926688268733,
  "oneMinute": 20.000035730063832,
  "fiveMinutes": 20.00000458740105,
  "fifteenMinutes": 19.94021043987079
}
```

This route will return 500 Internal Server Error if Spark 
cannot get the TPS of the server.

### /api/mspt
Returns the MSPT value for the past 10 seconds and 
1 minute.

Example Response:
```json
{
  "tenSeconds": {
    "min": 6.523605,
    "max": 2692.747083,
    "mean": 66.92731750515465,
    "median": 23.108289
  },
  "oneMinute": {
    "min": 6.523605,
    "max": 2692.747083,
    "mean": 66.92731750515465,
    "median": 23.108289
  }
}
```

This route will return 500 Internal Server Error if Spark 
cannot get the MSPT of the server.

## License
This plugin is licensed under the MIT License.
