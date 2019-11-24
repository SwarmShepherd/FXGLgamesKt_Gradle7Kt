

plugins {
    java
    kotlin("jvm") version "1.3.60"
    id("org.openjfx.javafxplugin") version "0.0.8"

    //Attempting to address GeometryWars Issues
    //id("oss.sonatype.org-snapshot")  
}

repositories {
    jcenter()
    mavenCentral()
    maven("http://nexus.gluonhq.com/nexus/content/repositories/releases")

    //Attempting to address GeometryWars Issues
    maven("http://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib"))

    //Attempting to address GeometryWars Issues
    implementation("org.apache.maven.plugins.shade.resource:ManifestResourceTransformer")
    
    implementation( "com.gluonhq.attach:storage:4.0.2")
    implementation( "com.gluonhq.attach:util:4.0.2")

    implementation( "com.github.almasb:fxgl:11.5")   
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
