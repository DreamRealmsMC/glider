# Installation

### Add glider as a dependency

{% tabs %}
{% tab title="Maven" %}
```
    <repositories>
    	<repository>
    	    <id>jitpack.io</id>
    	    <url>https://jitpack.io</url>
    	</repository>
    </repositories>
	
    <dependencies>
        <dependency>
            <groupId>com.github.DreamRealmsMC.glider</groupId>
            <artifactId>glider-velocity</artifactId>
            <version>${version}</version>
        </dependency>
    </dependencies>
```
{% endtab %}

{% tab title="Gradle" %}
```gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.DreamRealmsMC.glider:glider-velocity:${version}'
	}
	
	
```
{% endtab %}
{% endtabs %}



### Using glider API

Add glider as a dependecny to your velocity-plugin.json

```json
{
  "id": "yourplugin",
  "name": "your-plugin",
  "version": "0.1-SNAPSHOT",
  "dependencies": [
    {
      "id": "glider",
      "optional": false
    }
  ],
  "main": "you.yourname.yourplugin.YourPlugin"
}
```

```
GliderAPI gliderAPI = GliderProvider.getAPI();
```

You can then use gliderAPI to do whatever you need to.
