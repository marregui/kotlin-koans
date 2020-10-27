rootProject.name = "kotlin-koans"

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 10
    }
}