object NamceSpace {

    const val applicationId = "com.paysky.poststask"

    object Core {
        private const val core = "com.paysky.core"
        const val network = "$core.network"
        const val utils = "$core.utils"
        const val ui = "$core.ui" }

    object Feature {
        private const val feature = "com.paysky.feature"
        const val posts = "$feature.posts"
    }
}