// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    repositories {
        jcenter()
        google()
    }
}

task<Delete>("clean") {
    delete(project.buildDir)
}
