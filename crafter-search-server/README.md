### Crafter Search 

## Custom Configuration

To use a custom cofiguration you can place a YAML file in a `config` folder in
the same directory where the `search.jar` is placed.
Using that file you can override any property from Crafter Search or Spring Boot.

Example: `./config/application.yaml`

```YAML
# Change Solr Server
search.main.solr.server.url: http://localhost:8694/solr

# Change Crafter Search Port
search.server.port: 9080

# Chage Logging Levels
logging.level:
	org.springframework: DEBUG
	org.craftercms.search: DEBUG
	io.undertow: DEBUG
```

