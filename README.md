# OSPSdk

# Getting started

## Quick start with the demo app

1. Open Android Studio.
2. In Quick Start dialog choose Open project
3. In File dialog select OSPDemo folder.
4. Wait for the project to load. If Android studio asks you to reload project on startup, select Yes.
5. Run app.

When the app is running, you need to perform two steps. The first step is to obtain an sdkToken, which is a necessary parameter to start the process. A quick access is provided in the demo to obtain the sdkToken. In your environment, this parameter needs to be obtained from your server. The second step is to click the Start button to initiate the process.

The current SDK supports two nodes: ID photo mobile and live detection. Each node exists independently. You can selectively integrate specific features.  

## Integration

### Latest Version
1.0.0

### Dependence on settings

#### 1.Document

If you only integrate the document node, first set it in settings.gradle as follows:

```agsl
maven {
    allowInsecureProtocol = true
    url "http://localhost:8081/repository/poo/"
}
maven { url 'https://maven.microblink.com' }
```

Then, in your project's build.gradle, add the dependency.  

```agsl
implementation 'com.ooa.sdk:ment:1.0.21'
```

#### 2.Selfie

If you only integrate the selfie node, first set it in settings.gradle as follows:

```agsl
maven {
    allowInsecureProtocol = true
    url "http://localhost:8081/repository/poo/"
}

flatDir { dirs("${rootProject.projectDir}/app/libs") }

```

The 'dirs' path in the flatDir tag points to a local AAR package, which is used for facial recognition. You can obtain this AAR from the demo and place it in the appropriate directory within your project.  
Then, in your project's build.gradle, add the dependency.

```agsl
implementation 'com.ooa.sdk:selfie:1.0.21'
implementation(name:'facetec-sdk-9.6.64', ext:'aar')
```

#### 3.Document & Selfie

If you need to use both of them, then configure as follows:

```agsl
maven {
    allowInsecureProtocol = true
    url "http://localhost:8081/repository/poo/"
}
maven { url 'https://maven.microblink.com' }
flatDir { dirs("${rootProject.projectDir}/app/libs") }
```

```agsl
implementation 'com.ooa.sdk:self:1.0.21'
implementation 'com.ooa.sdk:ment:1.0.21'
implementation(name:'facetec-sdk-9.6.64', ext:'aar')
```

### Init SDK
You can initialize the SDK in the Application's onCreate method.
```kotlin
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        OSPSdk.instance.init(
            options = OSPOptions(
                context = this,
                key = "",
                sdkToken = "",
                processCallback = object : OSPProcessCallback {
                    override fun onComplete() {}

                    override fun onError(message: String?) {}

                    override fun onEvent(eventName: String, params: MutableMap<String, String>?) {}

                    override fun onExit(nodeCode: String) {}

                    override fun onReady() {}
                }
            )
        )
    }

}
```
Or you can initialize it when needed.The code in the demo is initialized only when it is really needed.

### Start a flow

When the SDK initialization is successful, register the nodes for the functionalities you need. For example, if you need to use Document and Selfie functionalities, then register these two nodes.

```kotlin
val instance = OSPSdk.instance
instance.registerNode(NodeCode.SELFIE, SelfieNode())
instance.registerNode(NodeCode.DOCUMENT_VERIFICATION, DocumentNode())
```
Afterwards, you can start a Flow process.
```kotlin
instance.startFlow(this@MainActivity)
```

# Device requirements
OSP SDK requires Android API level 21 or newer.