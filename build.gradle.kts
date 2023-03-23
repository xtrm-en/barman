plugins {
	id("fabric-loom") version "1.1-SNAPSHOT"
	`maven-publish`
}

base.archivesName.set(properties["archives_base_name"].toString())
version = properties["mod_version"].toString()
group = properties["maven_group"].toString()

dependencies {
	minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
	mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
	modImplementation("net.fabricmc:fabric-loader:${properties["loader_version"]}")

	modImplementation("net.fabricmc.fabric-api:fabric-api:${properties["fabric_version"]}")
}

java {
	withSourcesJar()
	withJavadocJar()

	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

tasks {
	withType<JavaCompile> {
		options.release.set(17)
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${base.archivesName.get()}"}
		}
	}

	processResources {
		inputs.property("version", version)

		filesMatching("fabric.mod.json") {
			expand("version" to version)
		}
	}
}