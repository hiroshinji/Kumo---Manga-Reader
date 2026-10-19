import java.net.URI

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Baixa o PDF.js 3.11.174 durante o build e o embute nos assets do APK.
// Depois de instalado, o app não precisa de internet para nada.
abstract class FetchPdfJs : DefaultTask() {
    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @TaskAction
    fun fetch() {
        val dir = outDir.get().asFile.resolve("lib")
        dir.mkdirs()
        val base = "https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/"
        for (name in listOf("pdf.min.js", "pdf.worker.min.js")) {
            val target = dir.resolve(name)
            var ok = false
            var last: Exception? = null
            for (attempt in 1..3) {
                try {
                    val conn = URI(base + name).toURL().openConnection()
                    conn.connectTimeout = 20000
                    conn.readTimeout = 60000
                    conn.getInputStream().use { input ->
                        target.outputStream().use { output -> input.copyTo(output) }
                    }
                    ok = target.length() > 50_000
                    if (ok) break
                } catch (e: Exception) {
                    last = e
                }
            }
            if (!ok) throw GradleException("Falha ao baixar $name de $base", last)
        }
    }
}

val fetchPdfJs = tasks.register<FetchPdfJs>("fetchPdfJs") {
    outDir.set(layout.buildDirectory.dir("generated/pdfjs-assets"))
}

androidComponents {
    onVariants { variant ->
        variant.sources.assets?.addGeneratedSourceDirectory(fetchPdfJs, FetchPdfJs::outDir)
    }
}

android {
    namespace = "app.kumo.reader"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.kumo.reader"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.webkit:webkit:1.12.1")
}
