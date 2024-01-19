# OSPSdk

# Getting started

## Quick start with the demo app

1. Open Android Studio.
2. In Quick Start dialog choose Open project
3. In File dialog select OSPDemo folder.
4. Wait for the project to load. If Android studio asks you to reload project on startup, select Yes.
5. Run app.

## Start a flow
When the app is running, you need to perform two steps. 
### Step1 Get an sdkToken
The first step is to obtain an sdkToken, which is a necessary parameter to start the process. A quick access is provided in the demo to obtain the sdkToken. In your environment, this parameter needs to be obtained from your server. 
### Step2 Start the flow
The second step is to click the Start button to initiate the process.

The current SDK supports two nodes: ID photo mobile and live detection. Each node exists independently. You can selectively integrate specific features.  

## Integration

### Latest Version
1.0.0

### Dependence on settings

#### 1.Document

If you only need to integrate the document node, you need to follow these 3 steps:   
1. Copy the core-release-1.0.0.aar and document-release-1.0.0.aar files from the demo into your project, these two files are located under the app->libs directory in the project.   
2. In settings.gradle, set the following:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url 'https://maven.microblink.com' }
        flatDir { dirs("${rootProject.projectDir}/app/libs") }
    }
}
```
3. In your project's build.gradle, add the dependency.  

```groovy
dependencies {
    implementation(name:'core-release-v1.0.0', ext:'aar')
    implementation(name:'document-release-1.0.0', ext:'aar')
}
```

#### 2.Selfie

If you only need to integrate the document node, you need to follow these 3 steps:
1. Copy the core-release-1.0.0.aar, document-release-1.0.0.aar and face-sdk-9.6.64.aar files from the demo into your project, these three files are located under the app->libs directory in the project.
2. In settings.gradle, set the following:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        flatDir { dirs("${rootProject.projectDir}/app/libs") }
    }
}

```

3. In your project's build.gradle, add the dependency.
```groovy
dependencies {
    implementation(name:'core-release-v1.0.0', ext:'aar')
    implementation(name:'selfie-release-1.0.0', ext:'aar')
    implementation(name:'face-sdk-9.6.64', ext:'aar')
}
```

#### 3.Document & Selfie

If you only need both of them, you need to follow these 3 steps:
1. Copy the core-release-1.0.0.aar, document-release-1.0.0.aar, selfie-release-1.0.0.aar and face-sdk-9.6.64.aar files from the demo into your project, these four files are located under the app->libs directory in the project.
2. In settings.gradle, set the following:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url 'https://maven.microblink.com' }
        flatDir { dirs("${rootProject.projectDir}/app/libs") }
    }
}
```

```groovy
dependencies {
    implementation(name:'face-sdk-9.6.64', ext:'aar')
    implementation(name:'core-release-v1.0.0', ext:'aar')
    implementation(name:'document-release-1.0.0', ext:'aar')
    implementation(name:'selfie-release-1.0.0', ext:'aar')
}
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
                sdkToken = "Your sdkToken",
                processCallback = object : OSPProcessCallback {
                    override fun onFinish(status: Boolean) {}

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
OSPSdk.instance
    .registerNode(NodeCode.SELFIE, SelfieNode())
    .registerNode(NodeCode.DOCUMENT_VERIFICATION, DocumentNode())
    .startFlow(this@MainActivity)
```

# Device requirements
OSP SDK requires Android API level 21 or newer.