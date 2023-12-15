import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
}

val libVersion = "1.0.2"
val libName = "ZOffice"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = libVersion
        ios.deploymentTarget = "12.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = libName
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    tasks {
        register("publishIOSLib") {
            description = "Publish iOs framework to the Cocoa Repo"
            doFirst {
                project.exec {
                    workingDir = File("$rootDir")
                    commandLine("./gradlew","clean").standardOutput
                    commandLine("./gradlew","podPublishXCFramework").standardOutput
                }
            }

            doLast {

                val cocoapodsDir = "$rootDir/cocoapods/release/$libVersion/"

                val deletePath = File(cocoapodsDir).delete()
                println("delete cocoapods dir is $deletePath")

                copy {
                    from("$rootDir/shared/build/cocoapods/publish/release")
                    into(cocoapodsDir)
                }
                File("$rootDir","shared.podspec").delete()
                File("$rootDir","$libName.podspec").delete()
                File("$rootDir","$libName.podspec").appendText("""
                Pod::Spec.new do |spec|
    spec.name                     = '$libName'
    spec.version                  = '$libVersion'
    spec.homepage                 = 'Link to the Shared Module homepage'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Some description for the Shared Module'
    spec.vendored_frameworks      = 'cocoapods/release/$libVersion/shared.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.0'
         
end
            """.trimIndent(),Charsets.UTF_8)


                project.exec {
                    workingDir = File("$rootDir")
                    commandLine(
                        "git",
                        "add",
                        "."
                    ).standardOutput
                }

                val dateFormatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
                project.exec {
                    workingDir = File("$rootDir")
                    commandLine(
                        "git",
                        "commit",
                        "-m",
                        "\"New dev release: ${libVersion}-${dateFormatter.format(Date())}\""
                    ).standardOutput
                }

                project.exec {
                    workingDir = File("$rootDir")
                    commandLine("git", "tag", libVersion).standardOutput
                }

                project.exec {
                    workingDir = File("$rootDir")
                    commandLine("git", "push", "origin", "main", "--tags").standardOutput
                }

            }
        }
    }
}

android {
    namespace = "com.example.firstkmmproject"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}