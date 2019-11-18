plugins {
    java
    kotlin("jvm") version "1.3.60"
    id("org.openjfx.javafxplugin") version "0.0.8"
}

repositories {
    jcenter()
    mavenCentral()
    maven("http://nexus.gluonhq.com/nexus/content/repositories/releases")   
}

dependencies {
    implementation(kotlin("stdlib")) 
    
    implementation( "com.gluonhq.attach:storage:4.0.2")
    implementation( "com.gluonhq.attach:util:4.0.2")

    implementation( "com.github.almasb:fxgl:11.5") 
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
