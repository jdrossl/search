### Crafter Search 





####  Logging Extended configuration
To change log level, extend logger you can use 
a file named `logging-ext.xml` that must be in the same 
path as the `search.jar` or add a environment variable
name `SEARCH_EXT_LOG_CONF` with the folder path where the `logging-ext.xml` file.

The file `logging-ext.xml` is scanned every 2 minutes for changes so a restart
to see the new log level (or any other logback confi change) is not needed.
Also is possible to add new loggers. 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<included>
   <!--<property name="root.level" value="INFO"/>
    <!-- <property name="search.level" value="INFO"/>
    <property name="commons.level" value="INFO"/>
    <property name="core.level" value="INFO"/>
    <property name="boot.level" value="INFO"/>
    <property name="undertow.level" value="INFO"/>
    <property name="xnio.level" value="INFO"/>
    <property name="root.level" value="INFO"/>
    <logge
    -->
</included>
```
