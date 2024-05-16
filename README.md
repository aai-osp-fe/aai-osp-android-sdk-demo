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

The current SDK only supports live bodies.

## Integration

### Latest Version
1.0.0

### Dependence on settings

To integrate the SDK, the following 3 steps are required:

1. Copy the osp-core-1.0.0.aar, osp-selfie-1.0.0.aar, osp-document-1.0.0.aar and osp-face-9.6.64.aar files from the demo into your project, these four files are located under the app->libs directory in the project.
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
    implementation(name:'osp-core-1.0.0.aar', ext:'aar')
    implementation(name:'osp-selfie-1.0.0', ext:'aar')
    implementation(name:'osp-document-1.0.0', ext:'aar')
    implementation(name:'osp-face-9.6.64', ext:'aar')
}
```

### Init SDK
You can initialize the SDK where you need it.
```kotlin
class MainActivity: AppcompatActivity() {

    fun startFlow(token: String) {
        super.onCreate(savedInstanceState)
        OSPSdk.instance.init(MyApp.getInstance()) // Application context
            .setSdkToken(token)
            .environment(OSPEnvironment.SANDBOX)
            .registerNode(NodeCode.SELFIE, SelfieNode())
            .registerNode(NodeCode.DOCUMENT_VERIFICATION, DocumentNode())
            .registerCallback(object : OSPProcessCallback {
                override fun onError(errorCode: String?, transId: String) {

                }

                override fun onEvent(
                    transId: String,
                    eventName: String,
                    params: MutableMap<String, String>?
                ) {

                }

                override fun onExit(nodeCode: String, transId: String) {
                    
                }

                override fun onFinish(status: Boolean, transId: String) {

                }

                override fun onReady() {

                }

            })
    }

}
```


### Start a flow

When the SDK initialization is successful, register the nodes for the functionalities you need. For example, if you need to use Selfie functionalities, then register the node.

```kotlin
OSPSdk.instance.startFlow(this@MainActivity)
```

# Device requirements
OSP SDK requires Android API level 21(Android5.0) or newer.