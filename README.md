# Why Did My Command Fail (WDMCF)

[ ![GitHub release](https://img.shields.io/github/v/release/ByMartrixx/wdmcf) ](https://github.com/ByMartrixx/wdmcf/releases/latest)

WDMCF is a mod that makes Minecraft always print the exception to the console when the message
"An unexpected error occurred trying to execute that command" is sent to the client. This mod is
intended to be used on development environments, you shouldn't install it on your normal
client/server.

## Usage
To add it to your development environment, add the following snippet to your `build.gradle`:
```groovy
repositories {
    maven {
        url 'https://maven.bymartrixx.me'
    }
}

dependencies {
    modImplementation 'me.bymartrixx:wdmcf:1.0.1'
}
```
