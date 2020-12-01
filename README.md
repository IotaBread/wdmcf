# Why Did My Command Fail (WDMCF)

[ ![Download](https://api.bintray.com/packages/bymartrixx/maven/wdmcf/images/download.svg) ](https://github.com/ByMartrixx/wdmcf/releases/tag/1.0.0)

WDMCF is a mod that makes Minecraft always print the exception to the console when the message "An unexpected error occurred trying to execute that command" is sent to the client. This mod is intended to be used on development environments, you shouldn't install it on your normal client/server.

## Usage
To add it to your development environment, add this to your `build.gradle`:
```groovy
repositories {
    maven {
        url 'https://dl.bintray.com/bymartrixx/maven'
    }
}

dependencies {
    modImplementation 'io.github.bymartrixx.wdmcf:wdmcf:1.0.1'
}
```
