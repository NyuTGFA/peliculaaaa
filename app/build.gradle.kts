plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.peliculaaaa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.peliculaaaa"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Dependencias de Android
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Dependencias de Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitgson)

    // Dependencias de Room
    implementation(libs.roomruntime)
    annotationProcessor(libs.roomcompiler)

    // Dependencias de Lifecycle (ViewModel y LiveData)
    implementation(libs.lifecycleviewmodelktx)
    implementation(libs.lifecyclelivedataktx)

    // Dependencia para RecyclerView
    implementation(libs.recyclerview)

    // Dependencia para Glide
    implementation(libs.glide)

    // Dependencia para Core KTX
    implementation(libs.corektx)

    // Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espressocore)
}

