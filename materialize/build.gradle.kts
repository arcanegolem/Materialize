import org.jreleaser.model.Active

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.jreleaser)
  alias(libs.plugins.maven.publish)
}

android {
  namespace = "arcanegolem.materialize"
  compileSdk = 35

  defaultConfig {
    minSdk = 24

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  publishing {
    singleVariant("release") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)

  implementation(platform(libs.coil.bom))
  implementation(libs.coil.compose)

  implementation(libs.retrofit)
  implementation(libs.okhttp)
}

version = "1.1.0"

publishing {
  publications {
    create<MavenPublication>("release") {
      groupId = "io.github.arcanegolem"
      artifactId = "materialize"

      pom {
        name.set("materialize")
        description.set("")
        url.set("https://github.com/arcanegolem/Materialize")
        issueManagement {
          url.set("https://github.com/arcanegolem/Materialize/issues")
        }

        scm {
          url.set("https://github.com/arcanegolem/Materialize")
          connection.set("scm:git://github.com/arcanegolem/Materialize.git")
          developerConnection.set("scm:git://github.com/arcanegolem/Materialize.git")
        }

        licenses {
          license {
            name.set("MIT License")
            url.set("https://github.com/arcanegolem/Materialize/blob/master/LICENSE")
            distribution.set("repo")
          }
        }

        developers {
          developer {
            id.set("arcanegolem")
            name.set("Mikhail Kleyzer")
            email.set("dopeflipper@gmail.com")
            url.set("")
          }
        }

        afterEvaluate {
          from(components["release"])
        }
      }
    }
  }
  repositories {
    maven {
      setUrl(layout.buildDirectory.dir("staging-deploy"))
    }
  }
}

jreleaser {
  jreleaser {
    project {
      inceptionYear = "2025"
      author("@arcanegolem")
    }
    gitRootSearch = true
    signing {
      active = Active.ALWAYS
      armored = true
      verify = true
    }
    release {
      github {
        skipTag = true
        sign = true
        branch = "master"
        branchPush = "master"
        overwrite = true
      }
    }
    deploy {
      maven {
        mavenCentral.create("sonatype") {
          active = Active.ALWAYS
          url = "https://central.sonatype.com/api/v1/publisher"
          stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
          setAuthorization("Basic")
          applyMavenCentralRules = false
          sign = true
          checksums = true
          sourceJar = true
          javadocJar = true
          retryDelay = 60
        }
      }
    }
  }
}