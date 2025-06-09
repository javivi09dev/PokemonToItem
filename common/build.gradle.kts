plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("maven-publish")
    id("signing")
}
group = "io.github.javivi09dev"
version = "0.2.0"

architectury {
    common("neoforge", "fabric")
}

loom {
    silentMojangMappingsLicense()
    mixin {
        defaultRefmapName.set("pokemontoitem-common-refmap.json")
    }
}
java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    // The following line declares the mojmap mappings, you may use other mappings as well
    mappings(loom.officialMojangMappings())
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

    modImplementation("org.apache.httpcomponents:httpclient:4.5.13")
    modImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}") { isTransitive = false }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            artifactId = "pokemontoitem"
            
            pom {
                name.set("PokemonToItem")
                description.set("Convertir Pokémon a items en Minecraft con Cobblemon")
                url.set("https://github.com/javivi09dev/PokemonToItem")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("javivi09dev")
                        name.set(project.findProperty("developer.name") as String? ?: "")
                        email.set(project.findProperty("developer.email") as String? ?: "")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/javivi09dev/PokemonToItem.git")
                    developerConnection.set("scm:git:ssh://github.com/javivi09dev/PokemonToItem.git")
                    url.set("https://github.com/javivi09dev/PokemonToItem")
                }
            }
        }
    }
    
    repositories {
        // Comentado repositorio remoto para usar solo Maven Local
        /*
        maven {
            name = "sonatype"
            url = uri("https://central.sonatype.com/api/v1/publisher/deployments/upload/")
            credentials {
                username = project.findProperty("sonatype.username") as String? ?: ""
                password = project.findProperty("sonatype.password") as String? ?: ""
            }
        }
        */
        
        // Repositorio local para desarrollo y testing
        mavenLocal()
    }
}

// Firma deshabilitada para desarrollo local
// signing {
//     sign(publishing.publications["maven"])
// }