plugins {
    application
    java
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    implementation("it.unibo.apice.scafiteam:scafi-core_2.12:0.3.2")
    implementation(project(":scala-proj"))
}

application {
    // Define the main class for the application
    mainClassName = "learning.scafi.integration.ScafiOnJava"
}